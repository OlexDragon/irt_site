/*
 * Copyright 2012-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package irt.stock.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import irt.stock.data.jpa.beans.Component;
import irt.stock.data.jpa.repositories.ComponentRepository;

@RestController
@RequestMapping("search")
public class SearchRestController {

	private final static int PN = 1;
	private final static int MFR_PN = 2;
	private final static int MFR = 4;
	private final static int VAL = 8;
	private final static int DESCRIPTION = 16;

	@Autowired
	private ComponentRepository componentRepository;

	@PostMapping
	public List<Component> search(
			@RequestParam(required=false) Long id,
			@RequestParam(required=false) String pn,
			@RequestParam(required=false) String mfrPN,
			@RequestParam(required=false) String mfr,
			@RequestParam(required=false) String description,
			@RequestParam(required=false) String val) {

		// By ID find only one component
		return Optional
				.ofNullable(id)
				.flatMap(i->componentRepository.findById(i))
				.map(component->{ List<Component> list = new ArrayList<>(); list.add(component); return list; })
				.orElseGet(()->{

					int switchCase = 0;
					String partNumber = null;

					if(Optional.ofNullable(pn).map(p->p.trim()).filter(p->!p.isEmpty()).isPresent()){
						partNumber = pn.toUpperCase().replaceAll("[^A-Z0-9]", "");
						switchCase += PN;
					}
					if(Optional.ofNullable(mfrPN).map(p->p.trim()).filter(p->!p.isEmpty()).isPresent())
						switchCase += MFR_PN;
					if( Optional.ofNullable(mfr).map(p->p.trim()).filter(p->!p.isEmpty()).isPresent())
						switchCase += MFR;
					if( Optional.ofNullable(description).map(p->p.trim()).filter(p->!p.isEmpty()).isPresent())
						switchCase += DESCRIPTION;
					if( Optional.ofNullable(val).map(p->p.trim()).filter(p->!p.isEmpty()).isPresent())
						switchCase += VAL;

					switch (switchCase) {

					case PN:
						return componentRepository.findByPartNumberContaining(partNumber);

					case MFR_PN:
						return componentRepository.findByManufPartNumberContaining(mfrPN);

					case MFR:
						return componentRepository.findByManufactureNameContaining(mfr);

					case VAL:
						return componentRepository.findByValueContaining(val);

					case DESCRIPTION:
						return componentRepository.findByDescriptionContaining(description);

					case PN + MFR_PN:
						return componentRepository.findByPartNumberContainingAndManufPartNumberContaining(partNumber, mfrPN);

					case PN + MFR:
						return componentRepository.findByPartNumberContainingAndManufactureNameContaining(partNumber, mfr);

					case PN + DESCRIPTION:
						return componentRepository.findByPartNumberContainingAndDescriptionContaining(partNumber, description);

					case PN + VAL:
						return componentRepository.findByPartNumberContainingAndValueContaining(partNumber, val);

					case PN + DESCRIPTION + VAL:
						return componentRepository.findByPartNumberContainingAndDescriptionContainingAndValueContaining(partNumber, description, val);

					case PN + MFR + DESCRIPTION:
						return componentRepository.findByPartNumberContainingAndManufactureNameContainingAndDescriptionContaining(partNumber, mfr, description);

					case PN + MFR + VAL:
						return componentRepository.findByPartNumberContainingAndManufactureNameContainingAndValueContaining(partNumber, mfr, val);

					case PN + MFR + DESCRIPTION + VAL:
						return componentRepository.findByPartNumberContainingAndManufactureNameContainingAndDescriptionContainingAndValueContaining(partNumber, mfr, description, val);

					case PN + MFR_PN + MFR:
						return componentRepository.findByPartNumberContainingAndManufPartNumberContainingAndManufactureNameContaining(partNumber, mfrPN, mfr);

					case PN + MFR_PN + MFR + DESCRIPTION:
						return componentRepository.findByPartNumberContainingAndManufPartNumberContainingAndManufactureNameContainingAndDescriptionContaining(partNumber, mfrPN, mfr, description);

					case PN + MFR_PN + DESCRIPTION:
						return componentRepository.findByPartNumberContainingAndManufPartNumberContainingAndDescriptionContaining(partNumber, mfrPN, description);

					case PN + MFR_PN + VAL:
						return componentRepository.findByPartNumberContainingAndManufPartNumberContainingAndValueContaining(partNumber, mfrPN, val);

					case PN + MFR_PN + DESCRIPTION + VAL:
						return componentRepository.findByPartNumberContainingAndManufPartNumberContainingAndDescriptionContainingAndValueContaining(partNumber, mfrPN, description, val);

					case PN + MFR_PN + MFR + VAL:
						return componentRepository.findByPartNumberContainingAndManufPartNumberContainingAndManufactureNameContainingAndValueContaining(partNumber, mfrPN, mfr, val);

					case PN + MFR_PN + MFR + DESCRIPTION + VAL:
						return componentRepository.findByPartNumberContainingAndManufPartNumberContainingAndManufactureNameContainingAndDescriptionContainingAndValueContaining(partNumber, mfrPN, mfr, description, val);

					case  MFR_PN + MFR:
						return componentRepository.findByManufPartNumberContainingAndManufactureNameContaining(mfrPN, mfr);

					case MFR_PN + DESCRIPTION:
						return componentRepository.findByManufPartNumberContainingAndDescriptionContaining(mfrPN, description);

					case MFR_PN + VAL:
						return componentRepository.findByManufPartNumberContainingAndValueContaining(mfrPN, val);

					case MFR_PN + DESCRIPTION + VAL:
						return componentRepository.findByManufPartNumberContainingAndDescriptionContainingAndValueContaining(mfrPN, description, val);

					case MFR_PN + MFR + DESCRIPTION:
						return componentRepository.findByManufPartNumberContainingAndManufactureNameContainingAndDescriptionContaining(mfrPN, mfr, description);

					case MFR_PN + MFR + VAL:
						return componentRepository.findByManufPartNumberContainingAndManufactureNameContainingAndValueContaining(mfrPN, mfr, val);

					case MFR_PN + MFR + DESCRIPTION + VAL:
						return componentRepository.findByManufPartNumberContainingAndManufactureNameContainingAndDescriptionContainingAndValueContaining(mfrPN, mfr, description, val);

					case MFR + DESCRIPTION:
						return componentRepository.findByManufactureNameContainingAndDescriptionContaining(mfr, description);

					case MFR + VAL:
						return componentRepository.findByManufactureNameContainingAndValueContaining(mfr, val);

					case MFR + DESCRIPTION + VAL:
						return componentRepository.findByManufactureNameContainingAndDescriptionContainingAndValueContaining(mfr, description, val);

					case DESCRIPTION + VAL:
						return componentRepository.findByDescriptionContainingAndValueContaining(description, val);
					}
					return null;
				});
	}
}
