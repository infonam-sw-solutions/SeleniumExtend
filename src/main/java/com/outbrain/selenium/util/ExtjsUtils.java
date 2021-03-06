package com.outbrain.selenium.util;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import com.thoughtworks.selenium.Selenium;

public class ExtjsUtils {
	
	public static enum Xtype {
		BOX, BUTTON, BUTTONGROUP, CHECKBOX, COLORPALETTE, COMBO, COMPONENT, CONTAINER, CYCLE, DATAVIEW, DATEFIELD, DATEPICKER, EDITOR, EDITORGRID, FLASH, GRID, LABEL, LISTVIEW, MENU, MENUITEM, NUMBERFIELD, PANEL, RADIO, SPLITBUTTON, SUPERBOXSELECT, TABPANEL, TEXTAREA, TEXTFIELD, TREEGRID, TREEPANEL, TRIGGER, VIEWPORT, WINDOW;
		
		public String getName() {
			return toString().toLowerCase();
		}
	}
	
	/**
	 * return reference to extjs component
	 * 
	 * @param componentId
	 * @return
	 */
	public static String getComponentById(final String componentId) {
		return "window.Ext.getCmp('"
				+ componentId
				+ "')";
	}
	
	public static String getComponentByTextOrLable(final Selenium selenium, final String textOrLable, final Xtype xtype) {
		String component = "";
		if (xtype != null) {
			if (textOrLable != null) {
				component = selenium.getEval(String.format("window.findComponentByText('%s','%s')", textOrLable, xtype.getName()));
			} else {
				component = selenium.getEval(String.format("window.findComponentByText(null,'%s')", xtype.getName()));
			}
		} else {
			
			component = selenium.getEval(String.format("window.findComponentByText('%s',null)", textOrLable));
		}
		
		return getComponentById(component);
		
	}
	
	/**
	 * just return array of id's be aware - (After you got the result you need to run getComponentExpression with component id to get the reference to the
	 * component
	 * 
	 * @param selenium
	 * @param xtype
	 * @return
	 */
	
	public static String[] getComponentIdsByType(final Selenium selenium, final Xtype xtype) {
		String[] component = null;
		if (xtype != null) {
			component = selenium.getEval(String.format("window.findComponentByText(null,'%s')", xtype.getName())).split(",");
		}
		return component;
		
	}
	
	public static String getComponentType(final Selenium selenium, final String textOrLable) {
		return selenium.getEval(String.format("%s.getXType()", getComponentByTextOrLable(selenium, textOrLable, null)));
	}
	
	public static String getComponentXpath(final WebDriver driver, final String element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		return (String) js.executeScript(String.format("return window.createXPathFromElement(%s)", element));
	}
	
	public static boolean waitForAjaxRequests(final Selenium selenium) {
		for (int second = 0; second <= 20; second++) {
			
			try {
				final String result = selenium.getEval("window.verifyNoAjaxCalls()");
				if ("false".equals(result)) {
					Thread.sleep(1000);
					
				} else {
					return true;
				}
				
			} catch (final Exception e) {
				
			}
		}
		return false;
	}
	
}
