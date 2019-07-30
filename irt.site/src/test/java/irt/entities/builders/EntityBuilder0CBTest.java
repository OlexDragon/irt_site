package irt.entities.builders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import irt.IrtSiteApp;
import irt.controllers.components.PartNumberForm;
import irt.entities.IrtComponentEntity;
import irt.entities.IrtComponentRepository;
import irt.entities.ManufactureEntity;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = IrtSiteApp.class)
public class EntityBuilder0CBTest {

	@Autowired EntityBuilder0CB builder;

	// Build Entity
	@Test
	public void buildFromNull() {
		assertNull(builder.build(null));
	}

	@Test
	public void buildWithFirst() {
		PartNumberForm pnf = new PartNumberForm();
		pnf.setFirst(1); //component
		IrtComponentEntity entity = builder.build(pnf);
		assertNotNull(entity);
		assertEquals("0CB_____________", entity.getPartNumber());
	}

	@Test
	public void buildWithFirstAndSecond() {
		PartNumberForm pnf = new PartNumberForm();
		pnf.setFirst(1);		//component
		pnf.setSecond("CB");	//cable
		IrtComponentEntity entity = builder.build(pnf);
		assertNotNull(entity);
		assertEquals("0CB_____________", entity.getPartNumber());
	}

	@Test
	public void buildWithMfrPN() {
		PartNumberForm pnf = new PartNumberForm();
		pnf.setFirst(1);		//component
		pnf.setSecond("CB");	//cable
		pnf.setFields(new String[]{ "NM", "JK", "RT", "22E3", "012", "Description", "BQ", "RF316-04BJ3-01SP1-0400"});	//manufacture part number
		IrtComponentEntity entity = builder.build(pnf);
		assertNotNull(entity);
		assertEquals("0CBNMJKRT22E3012", entity.getPartNumber());
		assertEquals("RF316-04BJ3-01SP1-0400", entity.getManufPartNumber());
		assertEquals("DESCRIPTION", entity.getDescription());
		assertEquals("Bergquist", entity.getManufacture().getName());
	}

	//Fill form
	@Test
	public void fillForm() {

		PartNumberForm pnf = new PartNumberForm();

		IrtComponentEntity entity = new IrtComponentEntity();
		entity.setDescription("Description");
		entity.setId(234L);
		ManufactureEntity m = new ManufactureEntity();
		m.setId("CB");
		entity.setManufacture(m);
		entity.setManufPartNumber("mfr pn");
		entity.setPartNumber("0CBBFSAFL33E1013");
		builder.fillForm(pnf, entity);

		assertEquals("0CB-BFSAFL-33E1-013", pnf.getPartNumber());
		assertEquals((Integer)1, pnf.getFirst());
		assertEquals("CB", pnf.getSecond());
		String[] fields = pnf.getFields();
		assertNotNull(fields);
		assertEquals(8, fields.length);
		assertEquals("BF", fields[0]);
		assertEquals("SA", fields[1]);
		assertEquals("FL", fields[2]);
		assertEquals("33 CM", fields[3]);
		assertEquals("013", fields[4]);
		assertEquals("DESCRIPTION", fields[5]);
		assertEquals("CB", fields[6]);
		assertEquals("MFR PN", fields[7]);
	}

	@Autowired IrtComponentRepository componentRepository;
	@Test
	public void entityBuilder1CB_FillFormTest2(){
		
		Optional<IrtComponentEntity> optional =componentRepository.findById(939L);
		optional.ifPresent(ce->{
			
			PartNumberForm pnf = new PartNumberForm();
			builder.fillForm(pnf, ce);

			assertEquals((Integer)1, pnf.getFirst());
			assertEquals(ce.getPartNumber().substring(1, 3), pnf.getSecond());

			String[] fields = pnf.getFields();
			assertNotNull(fields);
			assertEquals(8, fields.length);
			assertEquals(ce.getPartNumber().substring(3, 5), fields[0]);
			assertEquals(ce.getPartNumber().substring(5, 7), fields[1]);
			assertEquals(ce.getPartNumber().substring(7, 9), fields[2]);
			assertEquals(builder.toField(ce.getPartNumber().substring(9, 13)).toUpperCase(), fields[3]);
			assertEquals(ce.getPartNumber().substring(13), fields[4]);

			assertEquals(ce.getDescription(), fields[5]);
			assertEquals(ce.getManufacture().getId(), fields[6]);
			assertEquals(ce.getManufPartNumber(), fields[7]);

			assertNotEquals(ce.getPartNumber(), pnf.getPartNumber());
			assertEquals(ce.getPartNumber(), pnf.getPartNumber().replaceAll("-", ""));
		});
	}

	//exponent to cm and cm to exp. Converters test
	@Test
	public void valueToExp() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{

		Method method = EntityBuilderAbstract.class.getDeclaredMethod("toPartNumber", String.class);
		method.setAccessible(true);

		String r = (String) method.invoke(builder, (String)null);
		assertEquals("____", r);

		r = (String) method.invoke(builder, "e");
		assertEquals("____", r);

		r = (String) method.invoke(builder, "1e1");
		assertEquals("10E1", r);

		r = (String) method.invoke(builder, "1e");
		assertEquals("10E0", r);

		r = (String) method.invoke(builder, "e1");
		assertEquals("00E1", r);

		r = (String) method.invoke(builder, "01e1");
		assertEquals("01E1", r);

		r = (String) method.invoke(builder, "01e0");
		assertEquals("01E0", r);

		r = (String) method.invoke(builder, "33e1");
		assertEquals("33E1", r);
	}

	@Test
	public void valueToExp2() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{

		Method method = EntityBuilderAbstract.class.getDeclaredMethod("toPartNumber", String.class);
		method.setAccessible(true);

		String r = (String) method.invoke(builder, "0");
		assertEquals("00E0", r);

		r = (String) method.invoke(builder, "1");
		assertEquals("10E0", r);

		r = (String) method.invoke(builder, "10");
		assertEquals("10E1", r);

		r = (String) method.invoke(builder, "100");
		assertEquals("10E2", r);

		r = (String) method.invoke(builder, ".1");
		assertEquals("10EA", r);

		r = (String) method.invoke(builder, ".01");
		assertEquals("01EA", r);

		r = (String) method.invoke(builder, "1.1");
		assertEquals("11E0", r);
	}

	@Test
	public void valueToExp3() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{

		Method method = EntityBuilderAbstract.class.getDeclaredMethod("toPartNumber", String.class);
		method.setAccessible(true);

		String r = (String) method.invoke(builder, "0 cm");
		assertEquals("00E0", r);

		r = (String) method.invoke(builder, "1 cm");
		assertEquals("10E0", r);

		r = (String) method.invoke(builder, "10 cm");
		assertEquals("10E1", r);

		r = (String) method.invoke(builder, "100cm");
		assertEquals("10E2", r);

		r = (String) method.invoke(builder, ".1 cm");
		assertEquals("10EA", r);

		r = (String) method.invoke(builder, ".01 cm");
		assertEquals("01EA", r);

		r = (String) method.invoke(builder, "1cm1");
		assertEquals("11E0", r);
	}

	@Test
	public void valueToExp4() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{

		Method method = EntityBuilderAbstract.class.getDeclaredMethod("toPartNumber", String.class);
		method.setAccessible(true);

		String r = (String) method.invoke(builder, "0 m");
		assertEquals("00E0", r);

		r = (String) method.invoke(builder, "1 m");
		assertEquals("10E2", r);

		r = (String) method.invoke(builder, "10 m");
		assertEquals("10E3", r);

		r = (String) method.invoke(builder, "100m");
		assertEquals("10E4", r);

		r = (String) method.invoke(builder, ".1 m");
		assertEquals("10E1", r);

		r = (String) method.invoke(builder, ".01 m");
		assertEquals("10E0", r);

		r = (String) method.invoke(builder, "1m1");
		assertEquals("11E2", r);
	}

	@Test
	public void valueToExp5() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{

		Method method = EntityBuilderAbstract.class.getDeclaredMethod("toPartNumber", String.class);
		method.setAccessible(true);

		String r = (String) method.invoke(builder, "0 km");
		assertEquals("00E0", r);

		r = (String) method.invoke(builder, "1 km");
		assertEquals("10E5", r);

		r = (String) method.invoke(builder, "10 km");
		assertEquals("10E6", r);

		r = (String) method.invoke(builder, "100km");
		assertEquals("10E7", r);

		r = (String) method.invoke(builder, ".1 km");
		assertEquals("10E4", r);

		r = (String) method.invoke(builder, ".01 km");
		assertEquals("10E3", r);

		r = (String) method.invoke(builder, "1km1");
		assertEquals("11E5", r);
	}

	@Test
	public void getValueTest() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{

		Method method = EntityBuilderAbstract.class.getDeclaredMethod("getValue", String.class, int.class, char.class, boolean.class);
		method.setAccessible(true);

		String r = (String) method.invoke(builder, (String)null, 1, '0', true);
		assertEquals("0", r);

		r = (String) method.invoke(builder, "", 1, '0', true);
		assertEquals("0", r);

		r = (String) method.invoke(builder, "6", 1, '0', true);
		assertEquals("6", r);

		r = (String) method.invoke(builder, "6", 2, '0', true);
		assertEquals("60", r);

		r = (String) method.invoke(builder, "44", 4, '0', true);
		assertEquals("4400", r);
	}

	@Test
	public void expTovalueTest() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{

		Method method = EntityBuilderAbstract.class.getDeclaredMethod("toField", String.class);
		method.setAccessible(true);

		String r = (String) method.invoke(builder, (String)null);
		assertNull(r);

		r = (String) method.invoke(builder, "");
		assertNull(r);

		r = (String) method.invoke(builder, "6");
		assertEquals("0.6 cm", r);

		r = (String) method.invoke(builder, "60E0");
		assertEquals("6 cm", r);

		r = (String) method.invoke(builder, "44E6");
		assertEquals("44 km", r);

		r = (String) method.invoke(builder, "16E5");
		assertEquals("1.6 km", r);

		r = (String) method.invoke(builder, "65E3");
		assertEquals("65 m", r);

		r = (String) method.invoke(builder, "44E2");
		assertEquals("4.4 m", r);

		r = (String) method.invoke(builder, "44E1");
		assertEquals("44 cm", r);

		r = (String) method.invoke(builder, "44E0");
		assertEquals("4.4 cm", r);

		r = (String) method.invoke(builder, "44EA");
		assertEquals("0.44 cm", r);

		r = (String) method.invoke(builder, "33E1");
		assertEquals("33 cm", r);
	}
}
