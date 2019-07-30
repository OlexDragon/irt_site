package irt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import irt.controllers.components.interfaces.OptionFor;
import irt.controllers.components.options.OptionsFor0;
import irt.controllers.components.options.OptionsFor00A;
import irt.controllers.components.options.OptionsFor00C;
import irt.controllers.components.options.OptionsFor0CB;
import irt.controllers.components.options.OptionsFor0CO;
import irt.controllers.components.validators.Validator;
import irt.controllers.components.validators.Validator0;
import irt.controllers.components.validators.Validator00A;
import irt.controllers.components.validators.Validator00C;
import irt.controllers.components.validators.Validator0CB;
import irt.controllers.components.validators.Validator0CO;
import irt.entities.ArrayEntityRepository;
import irt.entities.builders.EntityBuilder;
import irt.entities.builders.EntityBuilder0;
import irt.entities.builders.EntityBuilder00A;
import irt.entities.builders.EntityBuilder00C;
import irt.entities.builders.EntityBuilder0CB;
import irt.entities.builders.EntityBuilder0CO;
import irt.entities.builders.EntityBuilderAbstract;

@Configuration
public class BeanDeclarations {

	@Autowired @Lazy protected ArrayEntityRepository arrayEntityRepository;

	//Entity Builders
	@Bean(name={"entityBuilder1", "entityBuilder0"})	@Lazy public EntityBuilder entityBuilder1(){ return new EntityBuilder0(); }
	@Bean(name={"entityBuilder1CB", "entityBuilder0CB"})@Lazy public EntityBuilder entityBuilder1CB(){ return new EntityBuilder0CB(); }
	@Bean(name={"entityBuilder10A", "entityBuilder00A"})@Lazy public EntityBuilder entityBuilder10A(){ return new EntityBuilder00A(); }
	@Bean(name={"entityBuilder10C", "entityBuilder00C"})@Lazy public EntityBuilder entityBuilder10C(){ return new EntityBuilder00C(arrayEntityRepository.findByKeyNameOrderByDescriptionAsc("size")); }
	@Bean(name={"entityBuilder1CO", "entityBuilder0CO"})@Lazy public EntityBuilder entityBuilder1CO(){ return new EntityBuilder0CO(); }

	//Validators
	@Bean(name={"validator1", "validator0"})	@Lazy public Validator validator1(){ return new Validator0(); }
	@Bean(name={"validator1CB", "validator0CB"})@Lazy public Validator validator1CB(){ return new Validator0CB(); }
	@Bean(name={"validator10A", "validator00A"})@Lazy public Validator validator10A(){ return new Validator00A(); }
	@Bean(name={"validator10C", "validator00C"})@Lazy public Validator validator10C(){ return new Validator00C(); }
	@Bean(name={"validator1CO", "validator0CO"})@Lazy public Validator validator1CO(){ return new Validator0CO(); }

	//Part Number options
	@Bean(name={"optionsFor1", "optionsFor0"})		@Lazy public OptionFor	option1(){ return new OptionsFor0((EntityBuilderAbstract)entityBuilder1()); }
	@Bean(name={"optionsFor1CB", "optionsFor0CB"})	@Lazy public OptionFor	option1CB(){ return new OptionsFor0CB((EntityBuilderAbstract)entityBuilder1CB()); }
	@Bean(name={"optionsFor10A", "optionsFor00A"})	@Lazy public OptionFor	option10A(){ return new OptionsFor00A((EntityBuilderAbstract)entityBuilder10A()); }
	@Bean(name={"optionsFor10C", "optionsFor00C"})	@Lazy public OptionFor	option10C(){ return new OptionsFor00C((EntityBuilderAbstract)entityBuilder10C()); }
	@Bean(name={"optionsFor1CO", "optionsFor0CO"})	@Lazy public OptionFor	option1CO(){ return new OptionsFor0CO((EntityBuilderAbstract)entityBuilder1CO()); }
}
