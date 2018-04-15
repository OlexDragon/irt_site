package irt.stock.web.controllers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import irt.stock.data.jpa.beans.ComponentAlternative;
import irt.stock.data.jpa.beans.Cost;
import irt.stock.data.jpa.beans.Cost.Currency;
import irt.stock.data.jpa.beans.Cost.OrderType;
import irt.stock.data.jpa.repositories.CompanyRepository;
import irt.stock.data.jpa.repositories.ComponentAlternativeRepository;
import irt.stock.data.jpa.repositories.ComponentMovementDetailRepository;
import irt.stock.data.jpa.repositories.ComponentMovementRepository;
import irt.stock.data.jpa.repositories.ComponentRepository;
import irt.stock.data.jpa.repositories.CostRepository;
import irt.stock.data.jpa.services.UserPrincipal;

@RestController
@RequestMapping("/component")
@Transactional
public class ComponentController {
	private final static Logger logger = LogManager.getLogger();

	@Autowired private CostRepository costRepository;
	@Autowired private CompanyRepository companyRepository;
	@Autowired private ComponentRepository componentRepository;
	@Autowired private ComponentAlternativeRepository componentAlternativeRepository;
	@Autowired private ComponentMovementRepository componentMovementRepository;
	@Autowired private ComponentMovementDetailRepository componentMovementDetailRepository;

	@PostMapping("price")
	public Response saveComponentQty(
			@RequestParam Long 		componentId,
			@RequestParam Long 		alternativeId,
			@RequestParam Long 		vendorId,
			@RequestParam Long 		forQty,
			@RequestParam BigDecimal price,
			@RequestParam Currency 	currency,
			@RequestParam OrderType orderType,
			@RequestParam String 	orderNumber,
			Principal  principal ) {

		StringBuilder response = new StringBuilder();

		Optional
				.of( SecurityContextHolder.getContext().getAuthentication())
				.filter(authentication->!(authentication instanceof AnonymousAuthenticationToken))
				.map(authentication->(UserPrincipal)authentication.getPrincipal())
				.ifPresent(up->{

					componentRepository
					.findById(componentId)
					.ifPresent(component->{

						companyRepository
						.findById(vendorId)
						.ifPresent(company->{
							
							BigDecimal p = price.setScale(8, RoundingMode.HALF_UP);

							ComponentAlternative alternative = componentAlternativeRepository.findById(alternativeId).orElse(null);

							Cost cost = costRepository.save(new Cost(component, alternative, company, forQty, p, currency, orderType, orderNumber));
							response.append("1) the Value saved to the COST table.\n");
						});
					});

				});

		return new Response(response.toString());
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
}
