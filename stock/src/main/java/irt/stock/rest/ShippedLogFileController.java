package irt.stock.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import irt.stock.data.jpa.beans.PartNumber;
import irt.stock.data.jpa.beans.production.CustomerOrder;
import irt.stock.data.jpa.beans.production.ProductionUnit;
import irt.stock.data.jpa.beans.production.SoftwareBuild;
import irt.stock.data.jpa.repositories.PartNumberRepository;
import irt.stock.data.jpa.repositories.production.CustomerOrderRepository;
import irt.stock.data.jpa.repositories.production.ProductionUnitRepository;
import irt.stock.data.jpa.repositories.production.SoftwareBuildRepository;
import irt.stock.rest.helpers.ShippedLogHelper;

@RestController
@RequestMapping("log_file")
public class ShippedLogFileController {

	private final static Logger logger =LogManager.getLogger();

	private static final int SERIAL_NUMBER_COLUMN = 1;
	private static final int PART_NUMBER_COLUMN = 2;
	private static final int DESCRIPTION_COLUMN = 4;
	private static final int SHIPMENT_DATE_COLUMN = 5;
	private static final int SOFT_BUILD_COLUMN = 9;

	private static final int INCLUDED_SERIAL_NUMBER = 10;
	private static final int INCLUDED_SOFRWARE_BUILD = 11;

	@Autowired private PartNumberRepository partNumberRepository;
	@Autowired private CustomerOrderRepository customerOrderRepository;
//	@Autowired private CustomerOrderCommentRepository commentRepository;
//	@Autowired private CustomerOrderDescriptionRepository descriptionRepository;
	@Autowired private SoftwareBuildRepository buildRepository;
	@Autowired private ProductionUnitRepository unitRepository;

	@PostMapping("scan")
	public Boolean scanLogFile() throws IOException{
		logger.info("Start scanning the file 'Shipped Log.xlsx'\\192.");

		Path path = Paths.get("z:", "Shipping", "Shipped Log.xlsx");
		File file = path.toFile();

		if(!file.exists()) {
			logger.info("The 'file Shipped Log.xls' does not exist.");
			return false;
		}

		FileInputStream excelFile = new FileInputStream(file);

		try(Workbook workbook = new XSSFWorkbook(excelFile);){

			final Sheet sheet = workbook.getSheetAt(0);
			final Iterator<Row> iterator = sheet.iterator();
			while (iterator.hasNext()) {

				Row currentRow = iterator.next();

				ShippedLogHelper

				//get custom order number
				.getCustometOrder(currentRow)	

				//get CO Class
				.map(
						orderNumber->customerOrderRepository
						.findByOrderNumber(orderNumber)
						.orElseGet(
								()->
								customerOrderRepository
								.save(new CustomerOrder(orderNumber))))

				//Set Date of Shipment
				.map(
						co->
						ShippedLogHelper
						.parseDate(currentRow.getCell(SHIPMENT_DATE_COLUMN))
						.filter(
								d->
								co.getClosed()==null || !co.getClosed().equals(d))
						.map(
								d->customerOrderRepository
								.save(co.setClosed(d)))
						.orElse(co))

				// Shipped units
				.ifPresent(
						co->{
							Cell serialNumberCell 	= currentRow.getCell(SERIAL_NUMBER_COLUMN);
							Cell partNumberCell 	= currentRow.getCell(PART_NUMBER_COLUMN);
							Cell softwareBuildCell 	= currentRow.getCell(SOFT_BUILD_COLUMN);
							Cell descriptionCell 	= currentRow.getCell(DESCRIPTION_COLUMN);

							Optional
							.ofNullable(ShippedLogHelper.getProductionUnit(serialNumberCell, partNumberCell, descriptionCell, softwareBuildCell))
							.map(prductionUnitFronDatabase())
							.ifPresent(
									pu->{
										// add production unit to the Customer order
										if(co.addProductionUnit(pu))
											customerOrderRepository.save(co);

										Cell snCell 	= currentRow.getCell(INCLUDED_SERIAL_NUMBER);
										Cell sbCell 	= currentRow.getCell(INCLUDED_SOFRWARE_BUILD);

										//Subunits
										final Optional<String> oSerialNumber = Optional.ofNullable(snCell).map(Cell::getStringCellValue).map(String::trim).filter(sn->sn.length()>5);

										oSerialNumber
										.filter(sn->!sn.contains("\n"))

										// One serial number in the cell
										.map(
												sn->
												Optional
												.ofNullable(ShippedLogHelper.getProductionUnit(snCell, null, null, sbCell))
												.map(prductionUnitFronDatabase())
												.map(subunit->{
													if(pu.addIncluded(subunit))
														return unitRepository.save(pu);
													return pu;
												}))

										// Multiple serial numbers in one cell
										.orElseGet(()->{

											final String[] serialNumbers 	= optionalStringToStream(oSerialNumber.filter(sn->sn.contains("\n"))).toArray(String[]::new);

											SoftwareBuild[] sb = optionalStringToStream(
																							Optional
																							.ofNullable(sbCell)
																							.map(Cell::getStringCellValue))

																		.map(s->ShippedLogHelper.stringToDate(s))
																		.map(
																				o->
																				o.map(buildRepository::findByBuild)
																				.filter(Optional::isPresent)
																				.map(Optional::get)
																				.orElseGet(
																						()->
																						o.map(d->buildRepository.save(new SoftwareBuild(d, null)))
																						.orElse(null)))
																		.toArray(SoftwareBuild[]::new);

											if(serialNumbers.length>sb.length){
												int length = sb.length;
												int lastPosition = length -1;
												sb = Arrays.copyOf(sb, serialNumbers.length);
												for(int i=serialNumbers.length; i<length; i++){
													sb[i]=sb[lastPosition];
												}
											}
											logger.debug("{} : {}", (Object[])serialNumbers, (Object[])sb);

											final SoftwareBuild[] builds = sb;

											IntStream
											.range(0, serialNumbers.length)
											.mapToObj(index->new ProductionUnit(serialNumbers[index], null, null, builds[index], null, null, null))
											.map(prductionUnitFronDatabase())
											.forEach(subUnit->{
												if(pu.addIncluded(subUnit))
													unitRepository.save(pu);
											});

											return null;
										});
									});
						});
			}
		}
		return true;
	}

	private Stream<String> optionalStringToStream(Optional<String> optional){
		return optional
				.map(sns->sns.replaceAll("\n", ","))
				.map(sns->sns.split(","))
				.map(Arrays::stream)
				.orElse(Stream.empty())
				.map(String::trim)
				.filter(sn->!sn.isEmpty());
	}

	private Function<ProductionUnit, ProductionUnit> prductionUnitFronDatabase() {
		return pu->
		Optional
		.ofNullable(pu.getSerialNumber())
		.flatMap(
				sn->
				unitRepository
				.findBySerialNumber(sn))
		.orElseGet(()->{

			//Get partNumber from Database
			Optional<PartNumber> oPartNumber = Optional.ofNullable(pu.getPartNumber());
			PartNumber partNumber = oPartNumber.flatMap(pn->partNumberRepository.findByPartNumber(pn.getPartNumber())).orElseGet(()->oPartNumber.map(pn->partNumberRepository.save(pn)).orElse(null));
			pu.setPartNumber(partNumber);

			//Set Software build date
			Optional<SoftwareBuild> oBuild = Optional.ofNullable(pu.getSoftwareBuild());
			SoftwareBuild softBuild = oBuild.flatMap(sb->buildRepository.findByBuild(sb.getBuild())).orElseGet(()->oBuild.map(sb->buildRepository.save(sb)).orElse(null));
			pu.setSoftwareBuild(softBuild);

			return unitRepository.save(pu);
		});
	}

//	private Optional<Object> getDescription(Row row) {
//
//		final CustomerOrderDescription coDescription = new CustomerOrderDescription(row.getCell(DESCRIPTION_COLUMN).getStringCellValue());
//
//		final String description = coDescription.getDescription();
//		if(description==null)
//			return Optional.empty();
//
//		return Optional.of(descriptionRepository.findByDescription(description).orElseGet(()->descriptionRepository.save(new CustomerOrderDescription(description))));
//	}
//
//	private Optional<long[]> getComments(Row roe) {
//
//		final int physicalNumberOfCells = roe.getPhysicalNumberOfCells();
//		long[] comments = new long[physicalNumberOfCells];
//		boolean doesNotHaveComments = true;
//
//		for(int i=0; i<physicalNumberOfCells; i++){
//
//			final int index = i;
//
//			Optional
//			.ofNullable(roe.getCell(i))
//			.map(Cell::getCellComment)
//			.map(Comment::getString)
//			.map(RichTextString::getString)
//			.map(String::trim)
//			.ifPresent(c->{
//
//				final CustomerOrderComment coComment = commentRepository.findByComment(c).orElseGet(()->commentRepository.save(new CustomerOrderComment(c)));
//				comments[index] = coComment.getId();
//			});
//		}
//
//		if(doesNotHaveComments)
//			return Optional.empty();
//
//		return Optional.of(comments);
//	}
}
