package xyz.migoo.selenium;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

/**
* @author xiaomi
 */
public class ActionsKeyboard {
    private static ActionsKeyboard uniqueInstance = null;
	private Actions action;
   
    private ActionsKeyboard(WebDriver driver){
        this.action = new Actions(driver);
    }
    
    public static synchronized ActionsKeyboard getInstance(WebDriver driver){
        if (uniqueInstance == null) {
            uniqueInstance = new ActionsKeyboard(driver);
        }
        return uniqueInstance;
     }
    /**
     * 操作键盘输入字符
     */
    public void type(WebElement element , String text) {
    	action.sendKeys(element, text).perform();
    }

    public void type( String text) {
        action.sendKeys(text).perform();
    }

    /**
     * 操作键盘的特殊按键
     * @param key
     * @param times
     */
    public void typeKey(String key,int times) {
       if (key.equalsIgnoreCase(RobotKeyboard.KEY_TAB)) {
           doTypeKey(Keys.TAB, times);
       } else if (key.equalsIgnoreCase(RobotKeyboard.KEY_ENTER)) {
           doTypeKey(Keys.ENTER, times);
       } else if (key.equalsIgnoreCase(RobotKeyboard.KEY_UP)) {
           doTypeKey(Keys.UP, times);
       } else if (key.equalsIgnoreCase(RobotKeyboard.KEY_DOWN)) {
           doTypeKey(Keys.DOWN, times);
       } else if (key.equalsIgnoreCase(RobotKeyboard.KEY_LEFT)) {
           doTypeKey(Keys.LEFT, times);
       } else if (key.equalsIgnoreCase(RobotKeyboard.KEY_RIGHT)) {
           doTypeKey(Keys.RIGHT, times);
       } else if (key.equalsIgnoreCase(RobotKeyboard.KEY_SHIFT)) {
           doTypeKey(Keys.SHIFT, times);
       } else if (key.equalsIgnoreCase(RobotKeyboard.KEY_ESC)) {
           doTypeKey(Keys.ESCAPE, times);
       } else if (key.equalsIgnoreCase(RobotKeyboard.KEY_BACKSPACE)) {
           doTypeKey(Keys.BACK_SPACE, times);
       } else {
           doTypeKey(null, times);
       }
    }
    
    private void doTypeKey(Keys keyCodes, int times) {
    	if(keyCodes == null) {
    		return; 
    	}
    	for(int i=0;i<times;i++){
    		action.keyDown(keyCodes);
        	action.keyUp(keyCodes);
    	}
    }
}
