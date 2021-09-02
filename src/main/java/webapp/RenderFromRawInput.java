package webapp;

import java.io.*;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.primefaces.model.file.UploadedFile;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "renderRaw")
@SessionScoped
public class RenderFromRawInput {

	private String jsonTextData;
	private String html;
	private String fileName;
	private Exception error;
	private UploadedFile file;

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public Exception getError() {
		return error;
	}

	public void setError(Exception error) {
		this.error = error;
	}

	public String getJsonTextData() {
		return jsonTextData;
	}

	public void setJsonTextData(String jsonTextData) {
		this.jsonTextData = jsonTextData;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public RenderFromRawInput() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
		LocalDateTime now = LocalDateTime.now();
		fileName = "Generated_Page_" + dtf.format(now);
	}

	public String upload() {
		String response = "404-page";
		if (file != null) {
			FacesMessage message = new FacesMessage("Successful", file.getFileName() + " is uploaded.");
			FacesContext.getCurrentInstance().addMessage(null, message);

			// JSON parser object to parse read file
			JSONParser jsonParser = new JSONParser();

			try (BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(file.getInputStream(), "UTF-8"))) {

				// Read JSON file
				Object obj = jsonParser.parse(bufferedReader);

				System.out.println(obj);
				response = processData(obj.toString());

			} catch (Exception ex) {
				System.out.println("Error uploading the file" + ex);
			}
		}
		return response;
	}
	
	public String renderHtml() {
		return processData(jsonTextData);
	}

	public String processData(String jsonData) {
		JsonToHTML jsonToHtml = new JsonToHTML();
		setHtml(jsonToHtml.getHtmlData(jsonData));
		System.out.println(fileName);

		try {
			generateHtmlPage(html);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.error = e;
			return "404-page";
		}

		return "generatedPage";

	}

	public void generateHtmlPage(String htmlContent) throws Exception {
		String path = "C:\\Users\\Arshad - Afsal\\eclipse-workspace\\webapp\\src\\main\\webapp\\";
		File htmlTemplateFile = new File(
				"C:\\Users\\Arshad - Afsal\\eclipse-workspace\\webapp\\src\\main\\webapp\\template.xhtml");
		String htmlString = FileUtils.readFileToString(htmlTemplateFile);
		String title = fileName;
		String body = htmlContent;
		htmlString = htmlString.replace("$title", title);
		htmlString = htmlString.replace("$body", body);
		File newHtmlFile = new File(path + fileName + ".html");
		FileUtils.writeStringToFile(newHtmlFile, htmlString);
	}

}
