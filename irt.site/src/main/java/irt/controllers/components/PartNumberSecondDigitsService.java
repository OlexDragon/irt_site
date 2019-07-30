package irt.controllers.components;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import irt.entities.SecondAndThirdDigitEntity;
import irt.entities.SecondAndThirdDigitRepository;

@Service
public class PartNumberSecondDigitsService {

	@Autowired SecondAndThirdDigitRepository secondAndThirdDigitRepository;
	public List<SecondAndThirdDigitEntity> setSecondDigitsEntities(Integer first, PartNumberComponent partNumberComponent) {

		if(first!=null){
			if(partNumberComponent.getSecondDiditsList()==null || partNumberComponent.getSecondDiditsList().get(0).getKey().getIdFirstDigit()!=first)
				partNumberComponent.setSecondDiditsList(secondAndThirdDigitRepository.findByKeyIdFirstDigitOrderByDescriptionAsc(first));
		}else
			partNumberComponent.setSecondDiditsList(null);

		return partNumberComponent.getSecondDiditsList();
	}
}