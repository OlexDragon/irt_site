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
import irt.data.components.PlasticParts;
import irt.data.components.PowerSupply;
import irt.data.components.RF_PowerFET;
import irt.data.components.Resistor;
import irt.data.components.Transistor;
import irt.data.components.Unknown;
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
import irt.data.row.RMWire;
import irt.data.row.RawMaterial;
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
import irt.work.TextWorker;
import irt.work.TextWorker.PartNumberFirstChar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class PartNumberDetails {

	Component component;

	private Logger logger = LogManager.getLogger();

	public PartNumberDetails(Component component) {
		this.component = component;
	}

	public Component getComponent(String componentId) {
		logger.entry(componentId, component);

		if (componentId!=null) {

			componentId = componentId.toUpperCase();
			if(componentId.length()==1){

				PartNumberFirstChar firstChar;
				char charAt = componentId.charAt(0);
				if(Character.isDigit(charAt)) {
					int parseInt = Integer.parseInt(componentId);
					firstChar = parseInt==0 ? PartNumberFirstChar.valueOf(charAt) : PartNumberFirstChar.valueOf(parseInt);
				} else
					firstChar = PartNumberFirstChar.valueOf(charAt);

				

				logger.trace("firstChar = {}", firstChar);
				if (firstChar != null
						&& (component==null
								|| firstChar.getFirstDigit().getFirstChar() != component.getClassId().charAt(0)
								|| component.getClassId().length()!=1)
				)

					switch (firstChar) {
					case TOP:
						component = new TopLevel();
						break;
					case ASSEMBLIES:
						component = new Assemblies();
						break;
					case BOARD:
						component = new Board();
						break;
					case METAL_PARTS:
						component = new MetalParts();
						break;
					case COMPONENT:
						component = new Component();
						break;
					case SCREWS:
						component = new Screws();
						break;
					case RAW_MATERIAL:
						component = new RawMaterial();
						break;
					default:
						component = new Unknown();
					}
			}else if(component==null || !componentId.equalsIgnoreCase(component.getClassId())){
				Integer id = Component.CLASS_NAME_ID.get(componentId);
				logger.trace("\n\tclass ID:\t{}\n\tcomponentId:\t{}", id, componentId);
				if(id!=null)
				switch (id) {
//Top ASSemblies
				case TextWorker.FREQUENCY_CONVERTER:
					component = new FrequencyConverter();
					break;
				case TextWorker.REDUNDANT:
					component = new RedundantSystem();
					break;
				case TextWorker.SSPA:
					component = new Sspa();
					break;
				case TextWorker.SSPB:
					component = new Sspb();
					break;
//Assemblies
				case TextWorker.CARRIER_ASSEMBLIES:
					component = new CarrierAssy();
					break;
				case TextWorker.COVER_ASSEMBLIES:
					component = new CoverAssy();
					break;
				case TextWorker.ENCLOSURE_ASSEMBLIES:
					component = new EnclosureAssy();
					break;
				case TextWorker.KIT_ASSEMBLIES:
					component = new KitAssy();
					break;
				case TextWorker.SWALL_ASSEMBLIES:
					component = new SWallAssy();
					break;
				case TextWorker.WG_ASSEMBLIES:
					component = new WGAssy();
					break;
//Board
				case TextWorker.BARE_BOARD:
					component = new BareBoard();
					break;
				case TextWorker.POPULATED_BOARD:
					component = new PopulatedBoard();
					break;
				case TextWorker.SOFT_LOADED_BOARD:
					component = new SoftwareLoadedPopulatedBoard();
					break;
				case TextWorker.SCHEMATIC:
					component = new Schematic();
					break;
				case TextWorker.GERBER:
					component = new Gerber();
					break;
				case TextWorker.PROJECT:
					component = new Project();
					break;
//Metal Parts
				case TextWorker.BRACKET:
					component = new Bracket();
					break;
				case TextWorker.CARRIER:
					component = new Carrier();
					break;
				case TextWorker.COVER:
					component = new Cover();
					break;
				case TextWorker.DEVICE_BLOK:
					component = new DeviceBlock();
					break;
				case TextWorker.ENCLOSURE:
					component = new Enclosure();
					break;
				case TextWorker.SHEET_METAL_BACKET:
					component = new SheetMetalBracket();
					break;
				case TextWorker.SHEET_METAL_ENCLOSURE:
					component = new SheetMetalEnclosure();
					break;
				case TextWorker.SHEET_METAL_FLAT:
					component = new SheetMetalFlat();
					break;
				case TextWorker.WALLS:
					component = new Walls();
					break;
				case TextWorker.HEATING:
					component = new Heating();
					break;
				case TextWorker.WAVEGUIDE:
					component = new Waveguide();
					break;
//Components
				case TextWorker.OTHER:
					component = new Other();
					break;
				case TextWorker.VARICAP:
					component = new Varicap();
					break;
				case TextWorker.TRANSISTOR:
					component = new Transistor();
					break;
				case TextWorker.POWER_SUPPLY:
					component = new PowerSupply();
					break;
				case TextWorker.FET:
					component = new RF_PowerFET();
					break;
				case TextWorker.ISOLATOR:
					component = new Isolator();
					break;
				case TextWorker.CONNECTOR:
					component = new Connector();
					break;
				case TextWorker.FAN:
					component = new Fan();
					break;
				case TextWorker.INDUCTOR:
					component = new Inductor();
					break;
				case TextWorker.RESISTOR:
					component = new Resistor();
					break;
				case TextWorker.CAPACITOR:
					component = new Capacitor();
					break;
				case TextWorker.VOLTAGE_REGULATOR:
					component = new VoltageRegulator();
					break;
				case TextWorker.MICROCONTROLLER:
					component = new MC();
					break;
				case TextWorker.IC_RF:
					component = new IC();
					break;
				case TextWorker.IC_NON_RF:
					component = new IC_NonRF();
					break;
				case TextWorker.DIODE:
					component = new Diode();
					break;
				case TextWorker.AMPLIFIER:
					component = new Amplifier();
					break;
				case TextWorker.WIRE_HARNESS:
					component = new WireHarness();
					break;
				case TextWorker.CABLES:
					component = new Cable();
					break;
				case TextWorker.GASKET:
					component = new Gasket();
					break;
//Screw
				case TextWorker.SCREW:
					component = new Screw();
					break;
				case TextWorker.WASHER:
					component = new Washer();
					break;
				case TextWorker.SPACER:
					component = new Spacer();
					break;
				case TextWorker.NUT:
					component = new Nut();
					break;
				case TextWorker.SCR_OTHER:
					component = new ScrOther();
					break;
				case TextWorker.PLASTIC_PLARTS:
					component = new PlasticParts();
					break;
//Raw Materials
				case TextWorker.WIRE:
					component = new RMWire();
					break;
				default:
					component = new Unknown();
				}
				else{
					component = new Unknown();
					String errorMessage = "The Part Number can not start from '"+componentId+"'.";
					component.setError(errorMessage);
					logger.error(errorMessage);
				}
			}
		}else if(component==null){
			component = new Unknown();
			String errorMessage = "The Part Number can not start from '" + componentId + "'.";
			component.setError(errorMessage);
			logger.error(errorMessage);
		}

		logger.trace("\n\tEXIT WITH\n\tcomponent:\t{}", component);
		return component;
	}

	public String getHTML() {
		String html		= "";

		if (getComponent() != null) {
			for (int i = 0; i < component.getTitleSize(); i++) {

				InputTitle it = component.getTitles().getInputTitle(i);

				html += " "+it.getName()+":";
				
				logger.debug("\n\t"
						+ "component:\t{}\n\t"
						+ "Input Type:\t{}",
						component,
						it);

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