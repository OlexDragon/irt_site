package irt.web.controllers;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import irt.web.IrtWebApp;
import irt.web.entities.part_number.repository.SecondAndThirdDigitRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = IrtWebApp.class)
@WebAppConfiguration
public class LoadComponentsControllerTest {

	private MockMvc mockMvc;
    @Autowired private WebApplicationContext webApplicationContext;
	@Autowired private SecondAndThirdDigitRepository secondAndThirdDigitRepository;

	@Before
    public void setup() throws Exception {
        mockMvc = webAppContextSetup(webApplicationContext)
        		.dispatchOptions(true)
                .build();
    }

	@Test
	public void test() throws Exception {

		mockMvc.perform(get("/load/part-number")
				.param("sequence", "2")
				.param("detailId", "OP")
				.param("pn", "0CB")
				.contentType("application/json;charset=UTF-8"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(3)));
	}

}
