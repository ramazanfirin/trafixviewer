package com.masterteknoloji.trafix.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.masterteknoloji.trafix.domain.dto.AnalyzeOrderSummaryVM;
import com.masterteknoloji.trafix.domain.dto.LineCrossedVM;
import com.masterteknoloji.trafix.domain.dto.LineSummaryVM;
import com.masterteknoloji.trafix.domain.dto.VideoRecordQueryVM;

@Service
public class DataService {
	
	
	public List<AnalyzeOrderSummaryVM> getAnalyzeOrderList() {
		List<AnalyzeOrderSummaryVM> result = new ArrayList<AnalyzeOrderSummaryVM>();
		
		
		try {
			URL url = new URL("http://localhost:8080/api/analyze-orders/getAllSummary");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			//con.setRequestProperty ("Authorization", "Bearer "+token);
			int status = con.getResponseCode();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer content = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			in.close();
					
			System.out.println(content.toString());
			ObjectMapper mapper = new ObjectMapper();
	        mapper.findAndRegisterModules();
	        result = mapper.readValue(content.toString(), new TypeReference<List<AnalyzeOrderSummaryVM>>() { });

//	  	    for (VideoRecordQueryVM videoRecordQueryVM : asList) {
//	  	    	result.add(videoRecordQueryVM.getDuration());
//			}
	  	    
			System.out.println("bitti");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		return result;
	}
	
	
	public List<LineSummaryVM> getLineList(Long id) {
		List<LineSummaryVM> result = new ArrayList<LineSummaryVM>();
		
		try {
			URL url = new URL("http://localhost:8080/api/lines/getLineSummaryListByScenarioId/"+id.toString());
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			//con.setRequestProperty ("Authorization", "Bearer "+token);
			int status = con.getResponseCode();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer content = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			in.close();
					
			System.out.println(content.toString());
			ObjectMapper mapper = new ObjectMapper();
	        mapper.findAndRegisterModules();

	        result = mapper.readValue(content.toString(), new TypeReference<List<LineSummaryVM>>() { });
			System.out.println("bitti");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		return result;
	}
	
	public List<LineCrossedVM> getCrossData(Long id) {
		List<LineCrossedVM> result = new ArrayList<LineCrossedVM>();
		
		try {
			URL url = new URL("http://localhost:8080/api/video-records/getVisulationData/"+id.toString());
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			//con.setRequestProperty ("Authorization", "Bearer "+token);
			int status = con.getResponseCode();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer content = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			in.close();
					
			System.out.println(content.toString());
			ObjectMapper mapper = new ObjectMapper();
	        mapper.findAndRegisterModules();

	        result = mapper.readValue(content.toString(), new TypeReference<List<LineCrossedVM>>() { });
			System.out.println("bitti");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		return result;
	}
}
