package com.masterteknoloji.trafix.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.masterteknoloji.trafix.domain.dto.AnalyzeOrderSummaryVM;
import com.masterteknoloji.trafix.domain.dto.LineCrossedVM;
import com.masterteknoloji.trafix.domain.dto.LineSummaryVM;
import com.masterteknoloji.trafix.util.Util;

@Service
public class DataService {
	
	String connectionUrl ;
	
	
	
	public DataService() throws IOException{
		super();
		// TODO Auto-generated constructor stub
		getConnectionUrl();
	}

	//@PostConstruct
	public void getConnectionUrl() throws IOException {
		Properties properties = Util.readPropertyFile();
		System.out.println("getConnectionUrl basladi");
		
		if(properties!=null)
			connectionUrl = properties.getProperty("connection.url");
		else
			connectionUrl = "http://localhost:8080";
		
		System.out.println("connectionUrl="+connectionUrl);
	}

	public List<AnalyzeOrderSummaryVM> getAnalyzeOrderList() throws IOException {
		List<AnalyzeOrderSummaryVM> result = new ArrayList<AnalyzeOrderSummaryVM>();
		System.out.println("getAnalyzeOrderList started");
		URL url = new URL(connectionUrl+"/api/analyze-orders/getAllSummary?size=100&sort=id%2Cdesc");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		// con.setRequestProperty ("Authorization", "Bearer "+token);
		int status = con.getResponseCode();

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer content = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			content.append(inputLine);
		}
		in.close();

		//System.out.println(content.toString());
		ObjectMapper mapper = new ObjectMapper();
		mapper.findAndRegisterModules();
		result = mapper.readValue(content.toString(), new TypeReference<List<AnalyzeOrderSummaryVM>>() {
		});

		System.out.println("bitti");

		return result;
	}

	public List<LineSummaryVM> getLineList(Long id) throws IOException {
		List<LineSummaryVM> result = new ArrayList<LineSummaryVM>();

		URL url = new URL(connectionUrl+"/api/lines/getLineSummaryListByScenarioId/" + id.toString());
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		// con.setRequestProperty ("Authorization", "Bearer "+token);
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

		result = mapper.readValue(content.toString(), new TypeReference<List<LineSummaryVM>>() {
		});
		System.out.println("bitti");

		return result;
	}

	public List<LineCrossedVM> getCrossData(Long id) throws IOException {
		List<LineCrossedVM> result = new ArrayList<LineCrossedVM>();

		URL url = new URL(connectionUrl+"/api/video-records/getVisulationData/" + id.toString());
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		// con.setRequestProperty ("Authorization", "Bearer "+token);
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

		result = mapper.readValue(content.toString(), new TypeReference<List<LineCrossedVM>>() {
		});
		System.out.println("bitti");

		return result;
	}
}
