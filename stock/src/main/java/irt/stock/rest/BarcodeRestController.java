package irt.stock.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import irt.stock.data.jpa.repositories.PartNumberRepository;

@RestController
@RequestMapping("/barcode")
public class BarcodeRestController {
	private final static Logger logger = LogManager.getLogger();

	@Autowired private PartNumberRepository partNumberRepository;

	@RequestMapping(value="part_number/{componentId}")
	public String postPartNambersLike(@PathVariable Long componentId){

		return partNumberRepository.findById(componentId)

				.map(
						component->{

							final String partNumber = component.getPartNumber();
							QRCodeWriter writer = new QRCodeWriter();

							try {

								Map<EncodeHintType, String> hints = new HashMap<>();
								hints.put(EncodeHintType.MARGIN, "0");
								final BitMatrix bitMatrix = writer.encode(partNumber, BarcodeFormat.QR_CODE, 1, 1, hints);
								final int column = bitMatrix.getWidth();
								final int rows = bitMatrix.getHeight();

								final String result = IntStream.range(0, rows)

										.mapToObj(
												roeIndex->
												IntStream.range(0, column)
												.filter(columnIndex->bitMatrix.get(columnIndex, roeIndex))
												.mapToObj(Integer::toString)
												.collect(Collectors.joining(",")))
										.collect(Collectors.joining("\n"));

								return result;

							} catch (WriterException e) {
								logger.catching(e);
							}
							return null;
						})

				.orElse(null);
	}
}
