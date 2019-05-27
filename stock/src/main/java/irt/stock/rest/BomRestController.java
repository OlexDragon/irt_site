package irt.stock.rest;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import irt.stock.data.jpa.beans.BomComponent;
import irt.stock.data.jpa.beans.BomComponentId;
import irt.stock.data.jpa.beans.BomHistory;
import irt.stock.data.jpa.beans.BomHistory.BomAction;
import irt.stock.data.jpa.beans.BomReference;
import irt.stock.data.jpa.beans.Company;
import irt.stock.data.jpa.beans.Company.CompanyType;
import irt.stock.data.jpa.beans.Component;
import irt.stock.data.jpa.beans.ComponentMovement;
import irt.stock.data.jpa.beans.ComponentMovementDetail;
import irt.stock.data.jpa.beans.PartNumber;
import irt.stock.data.jpa.beans.User;
import irt.stock.data.jpa.repositories.BomHistoryRepository;
import irt.stock.data.jpa.repositories.BomReferenceRepository;
import irt.stock.data.jpa.repositories.BomRepository;
import irt.stock.data.jpa.repositories.CompanyQtyRepository;
import irt.stock.data.jpa.repositories.CompanyRepository;
import irt.stock.data.jpa.repositories.ComponentMovementDetailRepository;
import irt.stock.data.jpa.repositories.ComponentMovementRepository;
import irt.stock.data.jpa.repositories.ComponentRepository;
import irt.stock.data.jpa.services.UserPrincipal;

@RestController
@RequestMapping("bom")
public class BomRestController {
	private final static Logger logger = LogManager.getLogger();

	@Autowired private BomRepository bomRepository;
	@Autowired private BomReferenceRepository bomReferenceRepository;
	@Autowired private BomHistoryRepository bomHistoryRepository;
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

										componentMovementDetailRepository.save(new ComponentMovementDetail(cMovement, component, qtyToRemove, oldQty));

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

	@PostMapping("delete/{pcaId}")
	public Boolean removeComponentForomTheBOM(@PathVariable Long pcaId, @RequestParam(required=false) Long componentId) {
		logger.traceEntry("{}",pcaId, componentId);

		if(componentId==null)
				return false;

		bomRepository.findById(new BomComponentId(pcaId, componentId))
		.ifPresent(bomComponent->{

			bomRepository.delete(bomComponent);
			Optional
			.of( SecurityContextHolder.getContext().getAuthentication())
			.filter(authentication->!(authentication instanceof AnonymousAuthenticationToken))
			.map(authentication->(UserPrincipal)authentication.getPrincipal())
			.ifPresent(up->{

				bomHistoryRepository.save(new BomHistory(pcaId, up.getUser().getId(), componentId, bomComponent.getReference().getId(), null, BomAction.DELETED));
			});
		});

		return true;
	}

	@PostMapping("delete_bom/{pcaId}")
	public Boolean removeBOM(@PathVariable Long pcaId) {
		logger.traceEntry("{}",pcaId);

		componentRepository.findById(pcaId)
		.ifPresent(component->{

			final List<BomComponent> bomComponents = bomRepository.findByIdTopComponentId(pcaId);
			bomRepository.deleteAll(bomComponents);

			component.clearManufPartNumber();
			componentRepository.save(component);

			Optional
			.of( SecurityContextHolder.getContext().getAuthentication())
			.filter(authentication->!(authentication instanceof AnonymousAuthenticationToken))
			.map(authentication->(UserPrincipal)authentication.getPrincipal())
			.ifPresent(up->{
				bomHistoryRepository.save(new BomHistory(pcaId, up.getUser().getId(), pcaId, null, null, BomAction.DELETED_BOM));
			});
		});

		return true;
	}

	@PostMapping("edit/{pcaId}")
	public Boolean editBomReferences(@PathVariable Long pcaId, @RequestParam(required=false) Long componentId, @RequestParam String references ) {
		logger.traceEntry("{}",pcaId, componentId, references);

		if(componentId==null ||  references==null)
				return false;

		return null != componentRepository
				.findById(componentId)
				.map(component -> {
					return saveReferences(pcaId, component, references);
				}).orElse(null);
	}

	@PostMapping("add/{pcaId}")
	public Boolean addComponentToTheBOM(@PathVariable Long pcaId, @RequestParam(required=false) Long componentId, @RequestParam(required=false) String partNumber, @RequestParam String refToAdd ) {
		logger.traceEntry("{}",pcaId, componentId, partNumber, refToAdd);

		if(!((componentId!=null || partNumber!=null) && refToAdd!=null))
				return false;

		return null != Optional
						.ofNullable(componentId)
						.map(cID->byComponentId(pcaId, componentId, refToAdd))
						.orElseGet(()->byPartNumber(pcaId, partNumber, refToAdd));
	}

	@PostMapping("replace/{pcaId}")
	public Boolean replaceComponentInTheBOM(@PathVariable Long pcaId, @RequestParam(required=false) Long exchangeable,  @RequestParam(required=false) Long replacementId,  @RequestParam(required=false) String replacementPN) {
		logger.traceEntry("{}",pcaId, exchangeable, replacementId, replacementPN);

		if(exchangeable==null || replacementId==null && replacementPN==null)
				return false;

		//find component to replace
		return bomRepository
				.findById(new BomComponentId(pcaId, exchangeable))
				.map(bomComponent->{
					final BomComponent newBomComponent = Optional
							.ofNullable(replacementId)
							.map(cID->Optional.of(exchangeable.equals(replacementId))
									.filter(equal->!equal)
									.map(notEqual->byComponentId(pcaId, replacementId, bomComponent.getReference().getReferences()))
									.orElse(null))

							.orElseGet(()->Optional.of(replacementPN)
									.map(pn->pn.toUpperCase().replaceAll("[^A-Z0-9]", ""))
									.filter(pn->!pn.equals(bomComponent.getComponent().getPartNumber()))
									.map(pn->byPartNumber(pcaId, replacementPN, bomComponent.getReference().getReferences()))
									.orElse(null));

					Optional.ofNullable(newBomComponent).ifPresent(nbc->{

						bomRepository.delete(bomComponent);

						Optional
						.of( SecurityContextHolder.getContext().getAuthentication())
						.filter(authentication->!(authentication instanceof AnonymousAuthenticationToken))
						.map(authentication->(UserPrincipal)authentication.getPrincipal())
						.ifPresent(up->{

							bomHistoryRepository.save(new BomHistory(pcaId, up.getUser().getId(), newBomComponent.getComponent().getId(), exchangeable, bomComponent.getReference().getId(), BomAction.REPLACED));
						});
					});

					return newBomComponent!=null;
				})
				.orElse(false);
	}

	private BomComponent byPartNumber(Long pcaId, String partNumber, String refToAdd) {
		return componentRepository
				.findByPartNumber(partNumber.toUpperCase().replaceAll("[^A-Z0-9]", ""))
				.map(component -> {
					return byComponent(pcaId, component, refToAdd);
				}).orElse(null);
	}

	private BomComponent byComponentId(Long pcaId, Long componentId, String refToAdd) {
		return componentRepository
				.findById(componentId)
				.map(component -> {
					return byComponent(pcaId, component, refToAdd);
				}).orElse(null);
	}

	private BomComponent saveReferences(Long pcaId, Component component, String references) {

		final String newRefSt = BomReference.referenceToIntStream(references).distinct().sorted().mapToObj(Integer::toString).collect(Collectors.joining(" "));

		return bomRepository.findById(new BomComponentId(pcaId, component.getId()))
		.map(bomComponent->{
			final BomReference newRef = bomReferenceRepository.findByReferences(newRefSt)
					.orElseGet(()->bomReferenceRepository.save(new BomReference(newRefSt)));

			final BomReference dbRef = bomComponent.getReference();
			if (!dbRef.equals(newRef)) {
				bomComponent.setReference(newRef);
				bomComponent = bomRepository.save(bomComponent);
				Optional
				.of( SecurityContextHolder.getContext().getAuthentication())
				.filter(authentication->!(authentication instanceof AnonymousAuthenticationToken))
				.map(authentication->(UserPrincipal)authentication.getPrincipal())
				.ifPresent(up->{
					bomHistoryRepository.save(new BomHistory(pcaId, up.getUser().getId(), component.getId(), dbRef.getId() , newRef.getId(), BomAction.CHANGED));
				});
			}
			return bomComponent;
		})
		.orElse(null);
	}
	private BomComponent byComponent(Long pcaId, Component component, String refToAdd) {

		final IntStream newStream = BomReference.referenceToIntStream(refToAdd);

		final Long componentId = component.getId();
		return bomRepository.findById(new BomComponentId(pcaId, componentId)).map(bomComponent -> {

			final BomReference dbRef = bomComponent.getReference();
			final IntStream dbStream = dbRef.referenceToIntStream();


			final IntStream concat = IntStream.concat(dbStream, newStream);
			final String referencesToSave = concat.distinct().sorted().mapToObj(Integer::toString).collect(Collectors.joining(" "));

			final BomReference newRef = bomReferenceRepository.findByReferences(referencesToSave)
					.orElseGet(()->bomReferenceRepository.save(new BomReference(referencesToSave)));

			if (!dbRef.equals(newRef)) {
				bomComponent.setReference(newRef);
				bomComponent = bomRepository.save(bomComponent);
				Optional
				.of( SecurityContextHolder.getContext().getAuthentication())
				.filter(authentication->!(authentication instanceof AnonymousAuthenticationToken))
				.map(authentication->(UserPrincipal)authentication.getPrincipal())
				.ifPresent(up->{
					bomHistoryRepository.save(new BomHistory(pcaId, up.getUser().getId(), componentId, dbRef.getId() , newRef.getId(), BomAction.CHANGED));
				});
			}
			return bomComponent;

		}).orElseGet(() -> {

			final String referencesToSave = newStream.distinct().sorted().mapToObj(Integer::toString).collect(Collectors.joining(" "));

			BomReference newRef = bomReferenceRepository.findByReferences(referencesToSave)
					.orElseGet(()->bomReferenceRepository.save(new BomReference(referencesToSave)));

			final BomComponent bomComponent = new BomComponent(pcaId, component, newRef);

			final BomComponent saved = bomRepository.save(bomComponent);
			Optional
			.of( SecurityContextHolder.getContext().getAuthentication())
			.filter(authentication->!(authentication instanceof AnonymousAuthenticationToken))
			.map(authentication->(UserPrincipal)authentication.getPrincipal())
			.ifPresent(up->{
				bomHistoryRepository.save(new BomHistory(pcaId, up.getUser().getId(), componentId, null, newRef.getId(), BomAction.INSERTED));
			});
			return saved;
		});
	}
}
