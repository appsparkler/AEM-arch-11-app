package com.sand.app.core.servlets;

//import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

import javax.servlet.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


//import org.apache.http

/**
 * @author praveen Servlet that writes some sample content into the response. It
 *         is mounted for all resources of a specific Sling resource type. The
 *         {@link SlingSafeMethodsServlet} shall be used for HTTP methods that
 *         are idempotent. For write operations use the
 *         {@link SlingAllMethodsServlet}.
 */

@SuppressWarnings("serial")
@Component(service = Servlet.class, property = {
		Constants.SERVICE_DESCRIPTION + "=The Guardiant | Get Morning News | Servlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/getMorningNews",
		"sling.servlet.extensions=" + "txt" })
public class MySimpleServlet extends SlingSafeMethodsServlet {
	private static final Logger LOGGER = LoggerFactory.getLogger(MySimpleServlet.class);

	@Override
	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServletException, IOException {

		try {
			final String guardianNewsAPI = "http://content.guardianapis.com/search?api-key=test&page-size=4&q=";
			JSONArray completeJson = new JSONArray();
			String paramValue = req.getParameter("tags");
			LOGGER.info("paramValue:" + paramValue);
			String[] allTags = paramValue.split("\\|");
			
			JSONObject appleJson = readJsonFromUrl(guardianNewsAPI + allTags[0]);
			JSONObject globeJson = readJsonFromUrl(guardianNewsAPI + allTags[1]);
			JSONObject googleJson = readJsonFromUrl(guardianNewsAPI + allTags[2]);
			JSONObject moneyJson = readJsonFromUrl(guardianNewsAPI + allTags[3]);

			completeJson
				.put(appleJson)
				.put(globeJson)
				.put(googleJson)
				.put(moneyJson);

			String[] topics = { allTags[0], allTags[1], allTags[2], allTags[3] };
			
			LOGGER.info("topics :  " + Arrays.toString(topics));
			
			String html = "", active = "";
			String[] dataHtml = new String[4];

			for (int i = 0; i < 4; i++) {

				// Adding 'active' class to the html of first tab
				if (i == 0)
					active = "active";
				else
					active = "";
				
				JSONObject x = completeJson.getJSONObject(i);
				
				LOGGER.info("x.toString() : " + x.toString(2));
				
				JSONObject response = x.getJSONObject("response"); // Getting
																	// response
																	// object
				JSONArray res = new JSONArray();
				res = response.getJSONArray("results"); // Reading results

				// Generating HTML to be returned to client side
				dataHtml[i] = "<div role=\"tabpanel\" class=\"tab-pane " + active + "\" id=\"" + topics[i] + "\">"
						+ "<ul class=\"list-group\">";
				for (int k = 0; k < res.length(); k++) {
					JSONObject p = res.getJSONObject(k);
					String data = "<li class=\"list-group-item\"><span class=\"glyphicon glyphicon-pushpin\">"
							+ "</span> <span class=\"label label-info\">" + p.get("sectionId") + "</span> "
							+ "<a target=\"_blank\" href = \"" + p.get("webUrl") + "\"> " + p.get("webTitle")
							+ "</a> </li>";

					dataHtml[i] = dataHtml[i] + data;
				}

				dataHtml[i] = dataHtml[i] + "</ul></div>";
				html = html + dataHtml[i];
			}
			resp.setContentType("text/html");
			resp.getWriter().write(html);
		} catch (Exception e) {

			LOGGER.info("Something went wrong:" + e.getMessage());
		}
	}

	private static String readAll(BufferedReader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			return json;
		} finally {
			is.close();
		}
	}
}