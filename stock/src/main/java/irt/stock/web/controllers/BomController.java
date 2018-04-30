package irt.stock.web.controllers;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import irt.stock.data.jpa.beans.BomComponent;
import irt.stock.data.jpa.beans.Company;
import irt.stock.data.jpa.beans.Company.CompanyType;
import irt.stock.data.jpa.beans.Component;
import irt.stock.data.jpa.beans.ComponentMovement;
import irt.stock.data.jpa.beans.ComponentMovementDetail;
import irt.stock.data.jpa.beans.PartNumber;
import irt.stock.data.jpa.beans.User;
import irt.stock.data.jpa.repositories.BomRepository;
import irt.stock.data.jpa.repositories.CompanyQtyRepository;
import irt.stock.data.jpa.repositories.CompanyRepository;
import irt.stock.data.jpa.repositories.ComponentMovementDetailRepository;
import irt.stock.data.jpa.repositories.ComponentMovementRepository;
import irt.stock.data.jpa.repositories.ComponentRepository;
import irt.stock.data.jpa.services.UserPrincipal;

@RestController
@RequestMapping("bom")
public class BomController {
	private final static Logger logger = LogManager.getLogger();

	@Autowired private BomRepository bomRepository;
	@Autowired private CompanyRepository companyRepository;
	@Autowired private CompanyQtyRepository companyQtyRepository;
	@Autowired private ComponentRepository componentRepository;
	@Autowired private ComponentMovementRepository componentMovementRepository;
	@Autowired private ComponentMovementDetailRepository componentMovementDetailRepository;

	@PostMapping("exists/{componentId}")
	public Boolean hasBom(@PathVariable Long componentId) {
		return bomRepository.existsByIdComponentId(componentId);
	}

	@PostMapping("top/{componentId}")
	public List<PartNumber> getTopPartNumbers(@PathVariable Long componentId) {
		return bomRepository.findTopPartNumberByIdComponentId(componentId);
	}

	@PostMapping("to_assembly/{pcaId}/{companyId}/{qty}")
	public Boolean movePcbToPca(@PathVariable Long pcaId, @PathVariable Long companyId, @PathVariable Long qty) {

		Optional
				.of( SecurityContextHolder.getContext().getAuthentication())
				.filter(authentication->!(authentication instanceof AnonymousAuthenticationToken))
				.map(authentication->(UserPrincipal)authentication.getPrincipal())
				.ifPresent(up->{
					logger.info("{}, pcaId: {}, companyId: {}, qty: {}", up, pcaId, companyId, qty);

					final User user = up.getUser();

					final List<BomComponent> bomComponents = bomRepository.findByIdTopComponentId(pcaId);

					if(!bomComponents.isEmpty()){

						bomComponents.stream().forEach(bomComponent -> {

							final Component component = bomComponent.getComponent();
							final Long id = component.getId();

							final String description = bomComponent.getTopPartNumber().getPartNumber();
							long bomQty = bomComponent.getReference().getQty();

							companyQtyRepository.findByIdIdCompaniesAndIdIdComponents(companyId, id)
							.filter(cq->Optional.ofNullable(cq.getQty()).orElse(0L)>0)
							.ifPresent(cq -> {

										final Company company = cq.getCompany();
										final Company assembled = companyRepository.findByType(CompanyType.ASSEMBLED).get(0);
										final long qtyToRemove = bomQty * qty;
										final long oldQty = cq.getQty();

										final ComponentMovement cMovement = componentMovementRepository.save(new ComponentMovement(user, company, assembled, description, new Date()));

										final ComponentMovementDetail movementDetail = componentMovementDetailRepository .save(new ComponentMovementDetail(cMovement, component, qtyToRemove, oldQty));

										cq.addQty(qtyToRemove * -1);

										companyQtyRepository.save(cq);
										logger.debug(cq);
									});
						});

						componentRepository
						.findById(pcaId)
						.ifPresent(pca->{
							companyRepository
							.findById(companyId)
							.ifPresent(fromCompany->{
								
								final Company toStock = companyRepository.findByType(CompanyType.STOCK).get(0);

								final ComponentMovement cMovement = componentMovementRepository.save(new ComponentMovement(user, fromCompany, toStock, "Assembled", new Date()));
								final long q = pca.getQty();
								componentMovementDetailRepository .save(new ComponentMovementDetail(cMovement, pca, qty, q));

								pca.addQty(qty);
								componentRepository.save(pca);
							});
						});
					}
				});

		return true;
	}
}
