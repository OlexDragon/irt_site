package irt.stock.rest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import irt.stock.data.jpa.beans.Company;
import irt.stock.data.jpa.beans.Company.CompanyType;
import irt.stock.data.jpa.beans.CompanyQty;
import irt.stock.data.jpa.beans.Component;
import irt.stock.data.jpa.beans.ComponentAlternative;
import irt.stock.data.jpa.beans.ComponentMovement;
import irt.stock.data.jpa.beans.ComponentMovementDetail;
import irt.stock.data.jpa.beans.ComponentObsolete;
import irt.stock.data.jpa.beans.Cost;
import irt.stock.data.jpa.beans.Cost.Currency;
import irt.stock.data.jpa.beans.Cost.OrderType;
import irt.stock.data.jpa.beans.CostHistory;
import irt.stock.data.jpa.beans.CostId;
import irt.stock.data.jpa.repositories.CompanyQtyRepository;
import irt.stock.data.jpa.repositories.CompanyRepository;
import irt.stock.data.jpa.repositories.ComponentAlternativeRepository;
import irt.stock.data.jpa.repositories.ComponentMovementDetailRepository;
import irt.stock.data.jpa.repositories.ComponentMovementRepository;
import irt.stock.data.jpa.repositories.ComponentRepository;
import irt.stock.data.jpa.repositories.CostHistoryRepository;
import irt.stock.data.jpa.repositories.CostRepository;
import irt.stock.data.jpa.services.UserPrincipal;
import irt.stock.rest.helpers.CoworkerReport;
import irt.stock.rest.helpers.StockReport;

@RestController
@RequestMapping("/component")
@Transactional
public class ComponentRestController {
	private final static Logger logger = LogManager.getLogger();

	@Autowired private CostRepository costRepository;
	@Autowired private CostHistoryRepository costHistoryRepository;
	@Autowired private CompanyRepository companyRepository;
	@Autowired private CompanyQtyRepository companyQtyRepository;
	@Autowired private ComponentRepository componentRepository;
	@Autowired private ComponentAlternativeRepository componentAlternativeRepository;
	@Autowired private ComponentMovementRepository componentMovementRepository;
	@Autowired private ComponentMovementDetailRepository componentMovementDetailRepository;

	@PostMapping("price")
	public Response saveComponentQty(
			@RequestParam 					Long 		componentId,
			@RequestParam 					Long 		alternativeId,
			@RequestParam 					Long 		vendorId,
			@RequestParam 					Long 		forQty,
			@RequestParam(required=false) 	BigDecimal	price,
			@RequestParam(required=false) 	Currency 	currency,
			@RequestParam					OrderType 	orderType,
			@RequestParam 					String 		orderNumber,
			@RequestParam 					ComponentAction action) {

		Optional
				.of( SecurityContextHolder.getContext().getAuthentication())
				.filter(authentication->!(authentication instanceof AnonymousAuthenticationToken))
				.map(authentication->(UserPrincipal)authentication.getPrincipal())
				.ifPresent(up->{
					logger.info("up, componentId: {}, alternativeId: {}, vendorId: {}, forQty: {}, price: {}, currency: {}, orderType: {}, orderNumber: {}, action: {}",
							componentId, alternativeId, vendorId, forQty, price, currency, orderType, orderNumber, action);

					componentRepository.findById(componentId)
					.ifPresent(component->{

						companyRepository
						.findById(vendorId)
						.ifPresent(froMCompany->{
							

							Date date;
							if(action==ComponentAction.ALL || action==ComponentAction.SAVE_PRICE) {

								if(price==null || currency==null)
									return;

								BigDecimal p = price.setScale(8, RoundingMode.HALF_UP);
								Cost cost = savePrice(alternativeId, forQty, currency, orderType, orderNumber, component, froMCompany, p);
								date = Optional.ofNullable(cost.getChangeDate()).orElse(new Date());

							}else
								date = new Date();

							// Return if do not need add quantity to stock
							final Company stock = companyRepository.findByType(CompanyType.STOCK).get(0);
							if(action==ComponentAction.ALL || action==ComponentAction.ADD_TO_CTOCK) {

								final ComponentMovement cMovement = componentMovementRepository.save(new ComponentMovement(up.getUser(), froMCompany, stock, orderType + " " + orderNumber, date));
								componentMovementDetailRepository.save(new ComponentMovementDetail(cMovement, component, forQty, component.getQty()));

								component.addQty(forQty);
								componentRepository.save(component);
							}

						});
					});

				});

		return new Response("Done");
	}
			
	@GetMapping("history")
	public List<ComponentMovementDetail> getComponentHistory(@RequestParam Long componentId) {
		return componentMovementDetailRepository.findByIdComponentIdOrderByIdComponentMovementDateTimeDesc(componentId);
	}

	@PostMapping("cost/delete/{componentId}/{alternativeId}/{companyId}/{forQty}")
	public Response getDeleteCost(@PathVariable Long componentId, @PathVariable Long alternativeId, @PathVariable Long companyId, @PathVariable Long forQty) {

		final CostId costId = new CostId(componentId, alternativeId, companyId, forQty);
		costRepository.deleteById(costId);
		return new Response("Done");
	}

	@GetMapping("price/history")
	public List<CostHistory> getPriceHistory(@RequestParam Long componentId) {
		return costHistoryRepository.findByIdComponentsOrderByChangeDateDesc(componentId);
	}

	@PostMapping("bulk")
	public Response moveToBulk(@RequestParam Long componentId, @RequestParam Long qty, @RequestParam String description) {

		Optional
				.of( SecurityContextHolder.getContext().getAuthentication())
				.filter(authentication->!(authentication instanceof AnonymousAuthenticationToken))
				.map(authentication->(UserPrincipal)authentication.getPrincipal())
				.ifPresent(up->{
					logger.info("{}, componentId: {}, qty: {}, description: {}", componentId, qty, description);

					componentRepository.findById(componentId)
					.ifPresent(component->{

						final Company bulk = companyRepository.findByType(CompanyType.BULK).get(0);
						final Company stock = companyRepository.findByType(CompanyType.STOCK).get(0);
						final Date date = new Date();

						final ComponentMovement cMovement = componentMovementRepository.save(new ComponentMovement(up.getUser(), stock, bulk, description, date));
						final Long oldQty = component.getQty();

						Long qtyToMove = Optional.of(qty).filter(q->q<=oldQty).orElse(oldQty);

						componentMovementDetailRepository.save(new ComponentMovementDetail(cMovement, component, qtyToMove, oldQty));

						component.addQty(qtyToMove * -1);
						componentRepository.save(component);
					});

				});
		return new Response("Done");
	}

	@PostMapping("mfr_to_bulk")
	public Response moveFromMfrToBulk(@RequestParam Long componentId, @RequestParam Long qty, @RequestParam String description, @RequestParam Long coMfr) {
		logger.traceEntry("{}",componentId, qty, description, coMfr);

		Optional
				.of( SecurityContextHolder.getContext().getAuthentication())
				.filter(authentication->!(authentication instanceof AnonymousAuthenticationToken))
				.map(authentication->(UserPrincipal)authentication.getPrincipal())
				.ifPresent(up->{
					logger.info("{}, componentId: {}, qty: {}, description: {}, coMfr ID: {}", up, componentId, qty, description, coMfr);

					componentRepository.findById(componentId)
					.ifPresent(component->{
						logger.debug(component);

						component.getCompanyQties().parallelStream().filter(cq->cq.getId().getIdCompanies()==coMfr).findAny()
						.ifPresent(cq->{
							logger.debug(cq);
							
							final Company bulk = companyRepository.findByType(CompanyType.BULK).get(0);
							final Company mfr = cq.getCompany();

							final ComponentMovement cMovement = componentMovementRepository.save(new ComponentMovement(up.getUser(), mfr, bulk, description, new Date()));
							final long oldQty = cq.getQty();

							Long qtyToMove = Optional.of(qty).filter(q->q<=oldQty).orElse(oldQty);

							componentMovementDetailRepository.save(new ComponentMovementDetail(cMovement, component, qtyToMove, oldQty));

							cq.addQty(qtyToMove * -1);
							companyQtyRepository.save(cq);
						});
					});

				});
		return new Response("Done");
	}

	@PostMapping("to_co_mfr")
	public Response moveToCoMfr(@RequestParam Long componentId, @RequestParam Long qty, @RequestParam Long companyId, @RequestParam String description) {

		Optional
				.of( SecurityContextHolder.getContext().getAuthentication())
				.filter(authentication->!(authentication instanceof AnonymousAuthenticationToken))
				.map(authentication->(UserPrincipal)authentication.getPrincipal())
				.ifPresent(up->{
					logger.info("{}, componentId: {}, qty: {}, description: {}, companyId: {}", componentId, qty, description, companyId);

					componentRepository.findById(componentId)
					.ifPresent(component->{

						companyRepository.findById(companyId)
						.ifPresent(co->{
							
							final Company stock = companyRepository.findByType(CompanyType.STOCK).get(0);
							final Date date = new Date();

							final Optional<CompanyQty> oCompanyQty = companyQtyRepository.findByIdIdCompaniesAndIdIdComponents(companyId, componentId);

							CompanyQty companyQty;
							if(oCompanyQty.isPresent()) {

								companyQty = oCompanyQty.get();
								companyQty.addQty(qty);

							}else
								companyQty = new CompanyQty(companyId, componentId, qty);

							companyQtyRepository.save(companyQty);

							final ComponentMovement cMovement = componentMovementRepository.save(new ComponentMovement(up.getUser(), stock, co, description, date));
							componentMovementDetailRepository.save(new ComponentMovementDetail(cMovement, component, qty, component.getQty()));

							component.addQty(qty * -1);
							componentRepository.save(component);
						});
					});
				});
		return new Response("Done");
	}

	@PostMapping("to_stock")
	public Response moveToStock(@RequestParam Long componentId, @RequestParam Long qty, @RequestParam Long companyId, @RequestParam String description) {

		Optional
				.of( SecurityContextHolder.getContext().getAuthentication())
				.filter(authentication->!(authentication instanceof AnonymousAuthenticationToken))
				.map(authentication->(UserPrincipal)authentication.getPrincipal())
				.ifPresent(up->{
					logger.info("{}, componentId: {}, qty: {}, description: {}, companyId: {}", componentId, qty, description, companyId);

					componentRepository.findById(componentId)
					.ifPresent(component->{

						companyRepository.findById(companyId)
						.ifPresent(co->{
							
							final Company stock = companyRepository.findByType(CompanyType.STOCK).get(0);
							final Date date = new Date();
							companyQtyRepository.findByIdIdCompaniesAndIdIdComponents(companyId, componentId)
							.ifPresent(cq->{

								cq.addQty(qty*-1);
								companyQtyRepository.save(cq);

								final ComponentMovement cMovement = componentMovementRepository.save(new ComponentMovement(up.getUser(), co, stock, description, date));
								componentMovementDetailRepository.save(new ComponentMovementDetail(cMovement, component, qty, component.getQty()));

								component.addQty(qty);
								componentRepository.save(component);
							});
						});
					});

				});
		return new Response("Done");
	}

	private Cost savePrice(Long alternativeId, Long forQty, Currency currency, OrderType orderType, String orderNumber, Component component, Company froMCompany, BigDecimal price) {

		ComponentAlternative alternative = componentAlternativeRepository.findById(alternativeId).orElse(null);
		Cost cost = costRepository.save(new Cost(component, alternative, froMCompany, forQty, price, currency, orderType, orderNumber));
		final CostHistory costHistory = new CostHistory(cost);
		costHistoryRepository.save(costHistory);
		return cost;
	}

	private final DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");

	@GetMapping(path = "report")
	public ResponseEntity<Resource> fullStockReport() throws IOException {

		AtomicInteger index = new AtomicInteger(1);
		Iterable<Component> allComponents = componentRepository.findAllByOrderByPartNumberAsc();

		final CoworkerReport coworkerReport = new CoworkerReport();
		final String collect = StreamSupport

				.stream( allComponents.spliterator(), false)

				.filter(
						component -> {
							ComponentObsolete componentObsolete = component.getComponentObsolete();
							return componentObsolete == null || componentObsolete.getStatus() == ComponentObsolete.Status.ACTIVE;
						})
				.map(
						component->{
							final int i = index.getAndIncrement();
							final String[] stockReport = StockReport.componentStockReport(component);
							stockReport[7] = String.format(stockReport[7], i);
							stockReport[8] = String.format(stockReport[8], i);
							stockReport[9] = String.format(stockReport[9], i);

							coworkerReport.add(
										stockReport[StockReport.PART_NUMBER],
										stockReport[StockReport.USD],
										stockReport[StockReport.CAD],
										stockReport[StockReport.UNKNOWN],
										component.getCompanyQties());

							return Arrays.stream(stockReport)
							.collect(Collectors.joining(","));
						})
				.collect(Collectors.joining("\n"));

		final String collectCoworkers = coworkerReport.getReport();

		Date date = new Date();
	    final String format = dateFormat.format(date);
		final byte[] zip = toZip(format, collect, collectCoworkers);

		ByteArrayResource resource = new ByteArrayResource(zip);
		return ResponseEntity
	    		.ok()
	            .header("Content-Disposition", "attachment; filename=report_" + format + ".zip")
	            .contentLength(zip.length)
	            .contentType(MediaType.parseMediaType("application/octet-stream"))
	            .body(resource);
	}

	private String[] fileNames = new String[]{"Stok", "Coworkers"};
	private byte[] toZip(String format, String... files) throws IOException {

		byte[] byteArray;

		try(	ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ZipOutputStream zos = new ZipOutputStream(baos);){

			for(int i=0; i<files.length; i++)
				putEntry(zos, fileNames[i] + format + ".csv", files[i].getBytes());

			byteArray = baos.toByteArray();
		}

		return byteArray;
	}

	private void putEntry(ZipOutputStream zos, String fileName, byte[] input) throws IOException{
		ZipEntry entry = new ZipEntry(fileName);
		entry.setSize(input.length);
		zos.putNextEntry(entry);
		zos.write(input);
		zos.closeEntry();
	}

	public class Response{

		public Response(String message) {
			this.message = message;
		}

		String message;

		public String getMessage() {
			return message;
		}
	}

	public enum ComponentAction{
		SAVE_PRICE,
		ADD_TO_CTOCK,
		ALL
	}
}
