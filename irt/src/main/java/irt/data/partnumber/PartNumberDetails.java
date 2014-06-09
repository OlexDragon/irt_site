package irt.data.partnumber;

import irt.data.assemblies.Assemblies;
import irt.data.assemblies.CarrierAssy;
import irt.data.assemblies.CoverAssy;
import irt.data.assemblies.EnclosureAssy;
import irt.data.assemblies.KitAssy;
import irt.data.assemblies.SWallAssy;
import irt.data.assemblies.WGAssy;
import irt.data.components.Amplifier;
import irt.data.components.Cable;
import irt.data.components.Capacitor;
import irt.data.components.Component;
import irt.data.components.Connector;
import irt.data.components.Diode;
import irt.data.components.Fan;
import irt.data.components.Gasket;
import irt.data.components.IC;
import irt.data.components.IC_NonRF;
import irt.data.components.Inductor;
import irt.data.components.Isolator;
import irt.data.components.MC;
import irt.data.components.Other;
import irt.data.components.PowerSupply;
import irt.data.components.RF_PowerFET;
import irt.data.components.Resistor;
import irt.data.components.Transistor;
import irt.data.components.Varicap;
import irt.data.components.VoltageRegulator;
import irt.data.components.WireHarness;
import irt.data.metal.Bracket;
import irt.data.metal.Carrier;
import irt.data.metal.Cover;
import irt.data.metal.DeviceBlock;
import irt.data.metal.Enclosure;
import irt.data.metal.Heating;
import irt.data.metal.MetalParts;
import irt.data.metal.SheetMetalBracket;
import irt.data.metal.SheetMetalEnclosure;
import irt.data.metal.SheetMetalFlat;
import irt.data.metal.Walls;
import irt.data.metal.Waveguide;
import irt.data.pcb.BareBoard;
import irt.data.pcb.Board;
import irt.data.pcb.Gerber;
import irt.data.pcb.PopulatedBoard;
import irt.data.pcb.Project;
import irt.data.pcb.Schematic;
import irt.data.pcb.SoftwareLoadedPopulatedBoard;
import irt.data.screws.Nut;
import irt.data.screws.ScrOther;
import irt.data.screws.Screw;
import irt.data.screws.Screws;
import irt.data.screws.Spacer;
import irt.data.screws.Washer;
import irt.data.top.FrequencyConverter;
import irt.data.top.RedundantSystem;
import irt.data.top.Sspa;
import irt.data.top.Sspb;
import irt.data.top.TopLevel;
import irt.web.Init;
import irt.work.InputTitle;
import irt.work.TextWork;


public class PartNumberDetails {

	Component component;

	//	private Logger logger = Logger.getLogger(this.getClass());

	public PartNumberDetails(Component component) {
		this.component = component;
	}

	public Component getComponent(String componentId) {

		if (componentId!=null && !componentId.equalsIgnoreCase((component!=null)?component.getClassId():null)) {

			componentId = componentId.toUpperCase();
			if(componentId.length()==1)
				switch (componentId) {
				case TextWork.TOP:
					component = new TopLevel();
					break;			
				case TextWork.ASSEMBLIES:
					component = new Assemblies();
					break;			
				case TextWork.BOARD:
					component = new Board();
					break;
				case TextWork.METAL_PARTS:
					component = new MetalParts();
					break;
				case TextWork.COMPONENT:
					component = new Component();
					break;
				case TextWork.SCREWS:
					component = new Screws();
				}
			else {
				Integer id = Component.CLASS_NAME_ID.get(componentId);
				if(id!=null)
				switch (id) {
//Top ASSemblies
				case TextWork.FREQUENCY_CONVERTER:
					component = new FrequencyConverter();
					break;
				case TextWork.REDUNDANT:
					component = new RedundantSystem();
					break;
				case TextWork.SSPA:
					component = new Sspa();
					break;
				case TextWork.SSPB:
					component = new Sspb();
					break;
//Assemblies
				case TextWork.CARRIER_ASSEMBLIES:
					component = new CarrierAssy();
					break;
				case TextWork.COVER_ASSEMBLIES:
					component = new CoverAssy();
					break;
				case TextWork.ENCLOSURE_ASSEMBLIES:
					component = new EnclosureAssy();
					break;
				case TextWork.KIT_ASSEMBLIES:
					component = new KitAssy();
					break;
				case TextWork.SWALL_ASSEMBLIES:
					component = new SWallAssy();
					break;
				case TextWork.WG_ASSEMBLIES:
					component = new WGAssy();
					break;
//Board
				case TextWork.BARE_BOARD:
					component = new BareBoard();
					break;
				case TextWork.POPULATED_BOARD:
					component = new PopulatedBoard();
					break;
				case TextWork.SOFT_LOADED_BOARD:
					component = new SoftwareLoadedPopulatedBoard();
					break;
				case TextWork.SCHEMATIC:
					component = new Schematic();
					break;
				case TextWork.GERBER:
					component = new Gerber();
					break;
				case TextWork.PROJECT:
					component = new Project();
					break;
//Metal Parts
				case TextWork.BRACKET:
					component = new Bracket();
					break;
				case TextWork.CARRIER:
					component = new Carrier();
					break;
				case TextWork.COVER:
					component = new Cover();
					break;
				case TextWork.DEVICE_BLOK:
					component = new DeviceBlock();
					break;
				case TextWork.ENCLOSURE:
					component = new Enclosure();
					break;
				case TextWork.SHEET_METAL_BACKET:
					component = new SheetMetalBracket();
					break;
				case TextWork.SHEET_METAL_ENCLOSURE:
					component = new SheetMetalEnclosure();
					break;
				case TextWork.SHEET_METAL_FLAT:
					component = new SheetMetalFlat();
					break;
				case TextWork.WALLS:
					component = new Walls();
					break;
				case TextWork.HEATING:
					component = new Heating();
					break;
				case TextWork.WAVEGUIDE:
					component = new Waveguide();
					break;
//Components
				case TextWork.OTHER:
					component = new Other();
					break;
				case TextWork.VARICAP:
					component = new Varicap();
					break;
				case TextWork.TRANSISTOR:
					component = new Transistor();
					break;
				case TextWork.POWER_SUPPLY:
					component = new PowerSupply();
					break;
				case TextWork.FET:
					component = new RF_PowerFET();
					break;
				case TextWork.ISOLATOR:
					component = new Isolator();
					break;
				case TextWork.CONNECTOR:
					component = new Connector();
					break;
				case TextWork.FAN:
					component = new Fan();
					break;
				case TextWork.INDUCTOR:
					component = new Inductor();
					break;
				case TextWork.RESISTOR:
					component = new Resistor();
					break;
				case TextWork.CAPACITOR:
					component = new Capacitor();
					break;
				case TextWork.VOLTAGE_REGULATOR:
					component = new VoltageRegulator();
					break;
				case TextWork.MICROCONTROLLER:
					component = new MC();
					break;
				case TextWork.IC_RF:
					component = new IC();
					break;
				case TextWork.IC_NON_RF:
					component = new IC_NonRF();
					break;
				case TextWork.DIODE:
					component = new Diode();
					break;
				case TextWork.AMPLIFIER:
					component = new Amplifier();
					break;
				case TextWork.WIRE_HARNESS:
					component = new WireHarness();
					break;
				case TextWork.CABLES:
					component = new Cable();
					break;
				case TextWork.GASKET:
					component = new Gasket();
					break;
//Screw
				case TextWork.SCREW:
					component = new Screw();
					break;
				case TextWork.WASHER:
					component = new Washer();
					break;
				case TextWork.SPACER:
					component = new Spacer();
					break;
				case TextWork.NUT:
					component = new Nut();
					break;
				case TextWork.SCR_OTHER:
					component = new ScrOther();
					break;
				default:
					component = new Component();
				}
				else
					component.setError("The Part Number can not start from '"+componentId+"'.");
			}
		}

		return component;
	}

	public String getHTML() {
		String html		= "";

		if (getComponent() != null) {
			for (int i = 0; i < component.getTitleSize(); i++) {

				InputTitle it = component.getTitles().getInputTitle(i);

				html += " "+it.getName()+":";
				
				switch (it.getInputType().toLowerCase()) {
				case "text":
					html += "<input type=\"text\" id=\"arg" + i
							+ "\" name=\"arg" + i + "\" value=\""
							+ component.getValue(i)
							+ "\" size=\"25\" "+(PartNumber.getBrowserId()==Init.CHROME ? "onclick" : "onfocus")+"=\"this.select();\"  onkeypress=\"return oneKeyPress(event,'submit-search')\" />\n";
					String dbColumnName = it.getDbColumnName();
					if(dbColumnName!=null)
						html += "<script type=\"text/javascript\">jQuery(function(){$(\"#arg"+i+"\").autocomplete(\"/irt/autocomplete/"+dbColumnName+".jsp\",{extraParams:{classId:\""+component.getClassId()+"\"}});});</script>";
					break;
				case "select":
					html += "<select id=\"arg" + i + "\" name=\"arg" + i
							+ "\" onchange=\"javascript:oneClick('submit_to_text');\">";
					html += "<option>Select</option>";
					html += component.getSelectOptionHTML(i);
					html += "</select>\n";
					break;
				case "label":
					html += "<label id=\"arg" + i + "\" >"
							+ component.getValue(i)
							+ " </label>\n";
					break;
				}
			}
		}
		return html;
	}

	public Component getComponent() {
		return component;
	}

	public void setComponent(Component component) {
		this.component = component;
	}
}