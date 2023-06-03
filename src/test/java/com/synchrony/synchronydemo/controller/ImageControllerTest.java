package com.synchrony.synchronydemo.controller;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.synchrony.synchronydemo.models.ImageDetails;
import com.synchrony.synchronydemo.security.JwtUtils;
import com.synchrony.synchronydemo.service.ImageDetailsService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ImageControllerTest extends AbstractTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	ImageDetailsService imageDetailsService;

	@MockBean
	private JwtUtils jwtTokenUtil;

	@Test
	public void viewAllImage() throws Exception {
		List<ImageDetails> imageList = new ArrayList<ImageDetails>();
		ImageDetails imageDetails = new ImageDetails();
		imageDetails.setId(1L);
		imageDetails.setImageUrl("http://imgur.com/abc.gif");
		imageDetails.setImageHash("fxvf");
		imageList.add(imageDetails);

		Mockito.when(jwtTokenUtil.getUsernameFromToken("jwtToken")).thenReturn("pranav");
		Mockito.when(imageDetailsService.getAllImages("pranav")).thenReturn(imageList);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/image").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();
		String content = result.getResponse().getContentAsString();
		ImageDetails[] imgList = super.mapFromJson(content, ImageDetails[].class);

		assertEquals(imgList[0].getImageHash(), "fxvf");
		assertEquals(HttpStatus.OK.value(), response.getStatus());

	}

}
