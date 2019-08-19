package irt.controllers.components;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import irt.controllers.components.interfaces.FirstDiditsList;
import irt.controllers.components.interfaces.SecondDiditsList;
import irt.entities.FirstDigitsEntity;
import irt.entities.FirstDigitsRepository;
import irt.entities.SecondAndThirdDigitEntity;

@Component
@Scope("session")
public class PartNumberComponent implements FirstDiditsList, SecondDiditsList {

	private static final String PART_NUMBERS = "Part Numbers";
	private String title = PART_NUMBERS;
	/**  @return page title */
	public String getPageTitle() { return title; }

	@Autowired FirstDigitsRepository firstDigitsRepository;
	private List<FirstDigitsEntity> firstDiditsList;
	@Override public List<FirstDigitsEntity> getFirstDiditsList() {
		if(firstDiditsList==null)
			firstDiditsList = firstDigitsRepository.findAll(Sort.by("description"));
		return firstDiditsList;
	}

	private List<SecondAndThirdDigitEntity> secondDiditsList;
	@Override public List<SecondAndThirdDigitEntity> getSecondDiditsList() { return secondDiditsList; }
	@Override public void setSecondDiditsList(List<SecondAndThirdDigitEntity> secondDiditsList) { this.secondDiditsList = secondDiditsList; }

//	private IrtComponentEntity componentEntity;
//	@Autowired IrtComponentRepository componentRepository;
//	public IrtComponentEntity getIrtComponentEntity(Long compomemtId) {return componentEntity; }
//	public void setIrtComponentEntity(Long compomemtId) {
//		if(compomemtId!=null){
//			if(componentEntity==null || componentEntity.getId()!=compomemtId)
//				componentEntity = componentRepository.findOne(compomemtId);
//		}else
//			componentEntity = null;
//	}

	private PartNumberForm partNumberForm;
	public PartNumberForm getPartNumberForm() { return partNumberForm; }
	public void setPartNumberForm(PartNumberForm partNumberForm) { 
		this.partNumberForm = partNumberForm;

		if(partNumberForm==null || partNumberForm.getFirst()==null)
			title = PART_NUMBERS;
		else if(partNumberForm.getSecond()==null){
			if(firstDiditsList==null)
				title = PART_NUMBERS;
			else{
				getFirstDescription(partNumberForm);
			}
		}else if(partNumberForm.getPartNumber()==null || partNumberForm.getPartNumber().length()<=3 || partNumberForm.getPartNumber().contains("_"))
			getSecondDescription(partNumberForm);
		else
			title = partNumberForm.getPartNumber();
	}

	private void getFirstDescription(PartNumberForm partNumberForm) {

		int indexOf = firstDiditsList.indexOf(new FirstDigitsEntity(partNumberForm.getFirst()));
		if(indexOf>=0)
			title = firstDiditsList.get(indexOf).getDescription();
		else
			title = PART_NUMBERS;
	}

	private void getSecondDescription(PartNumberForm partNumberForm) {

		int indexOf = secondDiditsList.indexOf(new SecondAndThirdDigitEntity(partNumberForm.getSecond(), partNumberForm.getFirst()));
		if(indexOf>=0)
			title = secondDiditsList.get(indexOf).getDescription();
		else
			getFirstDescription(partNumberForm);
	}

	@Override
	public String toString() {
		return "\n\t PartNumberComponent [title=" + title + ", firstDiditsList=" + firstDiditsList + ", secondDiditsList="
				+ secondDiditsList + "]";
	}
}
