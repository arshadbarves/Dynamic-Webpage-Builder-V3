package webapp;

import org.json.*;

import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Br;
import com.hp.gagawa.java.elements.Button;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.H3;
import com.hp.gagawa.java.elements.H4;
import com.hp.gagawa.java.elements.H5;
import com.hp.gagawa.java.elements.Input;
import com.hp.gagawa.java.elements.Label;
import com.hp.gagawa.java.elements.Option;
import com.hp.gagawa.java.elements.Select;
import com.hp.gagawa.java.elements.Text;

public class JsonToHTML {

	/**
	 * Get the JSON data formated in HTML
	 */
	public String getHtmlData(String strJsonData) {
		return jsonToHtml(new JSONObject(strJsonData));
	}

	/**
	 * convert json Data to structured Html text
	 * 
	 * @param json
	 * @return string
	 */

	Div mainDiv = new Div().setCSSClass("container");
	Br br = new Br();

	private String jsonToHtml(Object obj) {

		try {
			if (obj instanceof JSONObject) {
				JSONObject jsonObject = (JSONObject) obj;
				JSONObject applicationDetail = jsonObject.getJSONObject("ApplicationDetail");
				JSONArray subApplicationDetailList = applicationDetail.getJSONArray("SubApplicationDetailList");
				for (int i = 0; i < subApplicationDetailList.length(); i++) {
					JSONArray attributeDetailList = subApplicationDetailList.getJSONObject(i)
							.getJSONArray("AttributeDetailList");
					Object description = subApplicationDetailList.getJSONObject(i).get("Description");
					mainDiv.appendChild(br);
					H3 h3 = new H3();
					h3.appendText(description.toString());
					mainDiv.appendChild(h3, br);

					for (int j = 0; j < attributeDetailList.length(); j++) {

						JSONObject attributeDetail = attributeDetailList.getJSONObject(j);
						Object componentType = attributeDetail.get("ComponentType");
						Object isInvisible = attributeDetail.get("IsInvisible");
						if (isInvisible.toString().toUpperCase() != "Y") {
							switch (componentType.toString().toUpperCase()) {

							case "TEXT":
								inputTextTag(attributeDetail);
								break;
							case "HIDDEN":
								inputHiddenTag(attributeDetail);
								break;
							case "SELECT":
								inputSelectTag(attributeDetail);
								break;
							case "CHECKBOX":
								inputCheckboxTag(attributeDetail);
								break;
							case "SUBHEADING":
								subheadingTag(attributeDetail);
								break;
							case "OUTPUTTEXT":
								textTag(attributeDetail);
								break;
							case "ANCHOR":
								anchorTag(attributeDetail);
								break;
							case "BUTTON":
								buttonTag(attributeDetail);
								break;
							default:
								System.out.println(componentType.toString().toUpperCase());
							}
						}
					}
				}
			}
		} catch (JSONException e) {
			return e.getLocalizedMessage();
		}

		return mainDiv.write().toString();
	}

	private void buttonTag(JSONObject attributeDetail) {
		// Attributes

		String ComponentId = attributeDetail.get("ComponentId").toString();
		String AttributeName = attributeDetail.get("AttributeName").toString();
		String Size = attributeDetail.get("Size").toString();
		String StyleClass = attributeDetail.get("StyleClass").toString();
		String ComponentLabel = attributeDetail.get("ComponentLabel").toString();
		String IsDisabled = attributeDetail.get("IsDisabled").toString();
		String ComponentType = attributeDetail.get("ComponentType").toString().toLowerCase();
		
		Div div = new Div();
		div.setCSSClass("form-group row").setStyle("margin: 10px;");
		
		Button button = new Button();
		button.setType(ComponentType);
		button.setCSSClass("btn btn-primary");
		button.setName(AttributeName);
		button.setId(ComponentId);
		button.appendText(ComponentLabel);
		if (IsDisabled == "true") {
			button.setDisabled("disabled");
		}
		div.appendChild(button);
		mainDiv.appendChild(div);
	}

	private void anchorTag(JSONObject attributeDetail) {
		// Attributes

		String ComponentId = attributeDetail.get("ComponentId").toString();
		String AttributeName = attributeDetail.get("AttributeName").toString();
		String DefaultValue = attributeDetail.get("DefaultValue").toString();
		String ComponentLabel = attributeDetail.get("ComponentLabel").toString();

		Div div = new Div();
		div.setCSSClass("form-group row").setStyle("margin: 10px;");

		A a = new A();
		a.setId(ComponentId);
		a.setHref(DefaultValue);
		a.setName(AttributeName);
		a.setCSSClass("link-primary");
		a.appendText(ComponentLabel);
		div.appendChild(a);

		mainDiv.appendChild(div);
	}

	private void subheadingTag(JSONObject attributeDetail) {
		// Attributes

		String ComponentId = attributeDetail.get("ComponentId").toString();
		String ComponentLabel = attributeDetail.get("ComponentLabel").toString();
		mainDiv.appendChild(br);
		H5 h5 = new H5();
		h5.appendText(ComponentLabel);
		h5.setId(ComponentId);
		mainDiv.appendChild(h5, br);
	}

	private void textTag(JSONObject attributeDetail) {
		// Attributes
		String ComponentId = attributeDetail.get("ComponentId").toString();
		String AttributeName = attributeDetail.get("AttributeName").toString();
		String Size = attributeDetail.get("Size").toString();
		String StyleClass = attributeDetail.get("StyleClass").toString();
		String ComponentLabel = attributeDetail.get("ComponentLabel").toString();
		String IsDisabled = attributeDetail.get("IsDisabled").toString();
		String MaxLength = attributeDetail.get("MaxLength").toString();
		String IsRequired = attributeDetail.get("IsRequired").toString().toUpperCase();
		String RequiredMessage = attributeDetail.get("RequiredMessage").toString();

		Div div = new Div();
		div.setCSSClass("form-group row").setStyle("margin: 10px;");
		Label text = new Label();
		text.setCSSClass("form-label");
		text.setId(ComponentId);
		text.appendText(ComponentLabel);
		if (IsDisabled == "true") {
			text.setStyle("display: none;");
		}
		div.appendChild(new Div()
				.appendChild(
						new Label().appendText(ComponentLabel).setCSSClass("form-check-label").setFor(ComponentId)
								.setStyle("width:50"),
						new Div().appendText("*").setCSSClass("form-text").setStyle("color:red;width:50"))
				.setStyle("display:flex;").setCSSClass("col-sm-8"));
		mainDiv.appendChild(div);
	}

	private void inputCheckboxTag(JSONObject attributeDetail) {
		// Attributes

		String ComponentId = attributeDetail.get("ComponentId").toString();
		String AttributeName = attributeDetail.get("AttributeName").toString();
		String Size = attributeDetail.get("Size").toString();
		String StyleClass = attributeDetail.get("StyleClass").toString();
		String ComponentLabel = attributeDetail.get("ComponentLabel").toString();
		String IsDisabled = attributeDetail.get("IsDisabled").toString();
		String MaxLength = attributeDetail.get("MaxLength").toString();
		String ComponentType = attributeDetail.get("ComponentType").toString().toLowerCase();
		String IsRequired = attributeDetail.get("IsRequired").toString().toUpperCase();
		String RequiredMessage = attributeDetail.get("RequiredMessage").toString();

		Div div = new Div();
		div.setCSSClass("form-group form-check").setStyle("margin: 20px;");

		Input input = new Input();
		input.setType(ComponentType);
		input.setCSSClass("form-check-input");
		input.setName(AttributeName);
		input.setSize(Size);
		input.setId(ComponentId);
		if (IsDisabled == "true") {
			input.setDisabled("disabled");
		}
		input.setMaxlength(MaxLength);
		div.appendChild(input);

		div.appendChild(new Div()
				.appendChild(
						new Label().appendText(ComponentLabel).setCSSClass("form-check-label").setFor(ComponentId)
								.setStyle("width:50"),
						new Div().appendText("*").setCSSClass("form-text").setStyle("color:red;width:50"))
				.setStyle("display:flex;").setCSSClass("col-sm-4"));
		mainDiv.appendChild(div);
	}

	private void inputTextTag(JSONObject attributeDetail) {

		// Attributes

		String ComponentId = attributeDetail.get("ComponentId").toString();
		String AttributeName = attributeDetail.get("AttributeName").toString();
		String Size = attributeDetail.get("Size").toString();
		String StyleClass = attributeDetail.get("StyleClass").toString();
		String ComponentLabel = attributeDetail.get("ComponentLabel").toString();
		String IsDisabled = attributeDetail.get("IsDisabled").toString();
		String MaxLength = attributeDetail.get("MaxLength").toString();
		String ComponentType = attributeDetail.get("ComponentType").toString().toLowerCase();
		String IsRequired = attributeDetail.get("IsRequired").toString().toUpperCase();
		String RequiredMessage = attributeDetail.get("RequiredMessage").toString();

		Div div = new Div();
		div.setCSSClass("form-group row").setStyle("margin: 10px;");
		div.appendChild(new Div()
				.appendChild(
						new Label().appendText(ComponentLabel).setCSSClass("form-check-label").setFor(ComponentId)
								.setStyle("width:50"),
						new Div().appendText("*").setCSSClass("form-text").setStyle("color:red;width:50"))
				.setStyle("display:flex;").setCSSClass("col-sm-4"));

		Div inputDiv = new Div();
		inputDiv.setCSSClass("col-sm-8");
		div.appendChild(inputDiv);

		Input input = new Input();
		input.setType(ComponentType);
		input.setCSSClass("form-control");
		input.setName(AttributeName);
		input.setSize(Size);
		input.setId(ComponentId);
		if (IsDisabled == "true") {
			input.setDisabled("disabled");
		}
		input.setMaxlength(MaxLength);
		inputDiv.appendChild(input);
		mainDiv.appendChild(div);
	}

	private void inputHiddenTag(JSONObject attributeDetail) {

		// Attributes

		String ComponentId = attributeDetail.get("ComponentId").toString();
		String AttributeName = attributeDetail.get("AttributeName").toString();
		String Size = attributeDetail.get("Size").toString();
		String StyleClass = attributeDetail.get("StyleClass").toString();
		String ComponentLabel = attributeDetail.get("ComponentLabel").toString();
		String IsDisabled = attributeDetail.get("IsDisabled").toString();
		String MaxLength = attributeDetail.get("MaxLength").toString();
		String ComponentType = attributeDetail.get("ComponentType").toString().toLowerCase();
		String IsRequired = attributeDetail.get("IsRequired").toString().toUpperCase();
		String RequiredMessage = attributeDetail.get("RequiredMessage").toString();

		Div div = new Div();
		div.setCSSClass("form-group row").setStyle("margin: 10px;");
		div.setStyle("display: none;");
		div.appendChild(new Div()
				.appendChild(
						new Label().appendText(ComponentLabel).setCSSClass("form-check-label").setFor(ComponentId)
								.setStyle("width:50"),
						new Div().appendText("*").setCSSClass("form-text").setStyle("color:red;width:50"))
				.setStyle("display:flex;").setCSSClass("col-sm-4"));

		Div inputDiv = new Div();
		inputDiv.setCSSClass("col-sm-8");
		div.appendChild(inputDiv);

		Input input = new Input();
		input.setType(ComponentType);
		input.setCSSClass("form-control");
		input.setName(AttributeName);
		input.setSize(Size);
		input.setId(ComponentId);
		if (IsDisabled == "true") {
			input.setDisabled("disabled");
		}
		input.setMaxlength(MaxLength);
		inputDiv.appendChild(input);
		inputDiv.appendChild(new Div().appendText("*").setCSSClass("form-text").setStyle("color:red"));
		mainDiv.appendChild(div);
	}

	private void inputSelectTag(JSONObject attributeDetail) {
		JSONArray jsonArray = attributeDetail.getJSONArray("SubApplicationAttributeValueList");

		// Attributes

		String ComponentId = attributeDetail.get("ComponentId").toString();
		String AttributeName = attributeDetail.get("AttributeName").toString();
		String Size = attributeDetail.get("Size").toString();
		String StyleClass = attributeDetail.get("StyleClass").toString();
		String ComponentLabel = attributeDetail.get("ComponentLabel").toString();
		String IsDisabled = attributeDetail.get("IsDisabled").toString();
		String IsRequired = attributeDetail.get("IsRequired").toString().toUpperCase();
		String RequiredMessage = attributeDetail.get("RequiredMessage").toString();

		Div div = new Div();
		div.setCSSClass("form-group row").setStyle("margin: 10px;");
		div.appendChild(new Div()
				.appendChild(
						new Label().appendText(ComponentLabel).setCSSClass("form-check-label").setFor(ComponentId)
								.setStyle("width:50"),
						new Div().appendText("*").setCSSClass("form-text").setStyle("color:red;width:50"))
				.setStyle("display:flex;").setCSSClass("col-sm-4"));

		Div inputDiv = new Div();
		inputDiv.setCSSClass("col-sm-8");
		div.appendChild(inputDiv);

		Select select = new Select();
		select.setCSSClass("form-select");
		select.setName(AttributeName);
		select.setSize(Size);
		select.setId(ComponentId);
		if (IsDisabled == "true") {
			select.setDisabled("disabled");
		}
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject options = jsonArray.getJSONObject(i);

			String value = options.optString("Value");
			String displayValue = options.optString("DisplayValue");

			Option opt = new Option();
			opt.setValue(value);
			opt.appendChild(new Text(displayValue));

			select.appendChild(opt);

		} /* for */
		inputDiv.appendChild(select);
		mainDiv.appendChild(div);
	}

}
