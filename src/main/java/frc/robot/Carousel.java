package frc.robot;

import frc.robot.AgencySystem;

import com.revrobotics.CANDigitalInput;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANDigitalInput.LimitSwitchPolarity;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;


public class Carousel extends AgencySystem{

    protected Boolean advanceRequested = false;
    protected Boolean reverseAdvanceRequested = false;
    protected Boolean MODE_LOADING = true;



    private CANSparkMax m_motor;
    private CANDigitalInput m_forwardLimit;
    private CANDigitalInput m_reverseLimit;
    private CANDigitalInput.LimitSwitchPolarity m_forwardLimitPolarity;
    private CANDigitalInput.LimitSwitchPolarity m_reverseLimitPolarity;

    
    public Carousel(int deviceID, Boolean debug){
        new Carousel(deviceID, "CAN" + String.valueOf(deviceID), false);
    }
    public Carousel(int deviceID, String Name){
        new Carousel(deviceID, Name, false);
    }

    public Carousel(int deviceID, String name, Boolean debug){
    
    this.name = name;
    this.debug = debug;

    m_motor = new CANSparkMax(deviceID, MotorType.kBrushless);

    /**
     * The RestoreFactoryDefaults method can be used to reset the configuration parameters
     * in the SPARK MAX to their factory default state. If no argument is passed, these
     * parameters will not persist between power cycles
     */
    m_motor.restoreFactoryDefaults();

    /**
     * A CANDigitalInput object is constructed using the getForwardLimitSwitch() or
     * getReverseLimitSwitch() method on an existing CANSparkMax object, depending
     * on which direction you would like to limit
     * 
     * Limit switches can be configured to one of two polarities:
     *  com.revrobotics.CANDigitalInput.LimitSwitchPolarity.kNormallyOpen
     *  com.revrobotics.CANDigitalInput.LimitSwitchPolarity.kNormallyClosed
     * 
     */

    m_forwardLimitPolarity = CANDigitalInput.LimitSwitchPolarity.kNormallyOpen;
    m_forwardLimit = new CANDigitalInput(m_motor,CANDigitalInput.LimitSwitch.kForward, m_forwardLimitPolarity);
    m_forwardLimit = m_motor.getForwardLimitSwitch(m_forwardLimitPolarity);
    
    m_reverseLimitPolarity = CANDigitalInput.LimitSwitchPolarity.kNormallyClosed;
    m_reverseLimit = new CANDigitalInput(m_motor,CANDigitalInput.LimitSwitch.kReverse, m_reverseLimitPolarity);
    m_reverseLimit = m_motor.getReverseLimitSwitch(m_reverseLimitPolarity);

    /**
     * Limit switches are enabled by default when the are intialized. They can be disabled
     * by calling enableLimitSwitch(false) on a CANDigitalInput object
     * 
     * Limit switches can be reenabled by calling enableLimitSwitch(true)
     * 
     * The isLimitSwitchEnabled() method can be used to check if the limit switch is enabled
     */
    m_forwardLimit.enableLimitSwitch(false);
    m_reverseLimit.enableLimitSwitch(true);
    }

    public Boolean hasBall1() {
        //TODO: Limit Switch Implementation
        return false;
    }

    public Boolean hasBall2() {
         //TODO: Limit Switch Implementation
        return false;
    }

    public Boolean hasBall3() {
         //TODO: Limit Switch Implementation
        return false;
    }

    public void requestForwardAdvance() {
        advanceRequested = true;


    }

    public void requestReverseAdvance() {
        reverseAdvanceRequested = true;
    }

    public void loadBalls(){
        MODE_LOADING = true;
    }

    public void unloadBalls(){
        MODE_LOADING = false;
    }


    public void teleopPeriodic() {
        boolean LIMIT_SWITCH_IS_ENABLED = m_forwardLimit.get();
        boolean REVERSE_LIMIT_ENABLED = m_reverseLimit.get();

        if (MODE_LOADING){
            m_motor.set(0.06);
        }
        else if (!MODE_LOADING){
            m_motor.set(-0.06);
        }
    
        System.out.println("Carousel Request " + advanceRequested);
        System.out.println("Polarity " + m_forwardLimitPolarity);
        System.out.println("Limit Switch Enabled " + LIMIT_SWITCH_IS_ENABLED);

        if (advanceRequested && m_forwardLimitPolarity == CANDigitalInput.LimitSwitchPolarity.kNormallyClosed && LIMIT_SWITCH_IS_ENABLED){
            m_forwardLimitPolarity = CANDigitalInput.LimitSwitchPolarity.kNormallyOpen;
            m_forwardLimit = m_motor.getForwardLimitSwitch(m_forwardLimitPolarity);  
        }
        
        else if(LIMIT_SWITCH_IS_ENABLED && m_forwardLimitPolarity == CANDigitalInput.LimitSwitchPolarity.kNormallyOpen){
            m_forwardLimitPolarity = CANDigitalInput.LimitSwitchPolarity.kNormallyClosed;
            m_forwardLimit = m_motor.getForwardLimitSwitch(m_forwardLimitPolarity);
            advanceRequested = false;
        }

        //Reverse Advance Request
        if (reverseAdvanceRequested && m_reverseLimitPolarity == CANDigitalInput.LimitSwitchPolarity.kNormallyClosed && REVERSE_LIMIT_ENABLED){
            m_reverseLimitPolarity = CANDigitalInput.LimitSwitchPolarity.kNormallyOpen;
            m_reverseLimit = m_motor.getReverseLimitSwitch(m_reverseLimitPolarity);
          
    
        }
        
        else if(REVERSE_LIMIT_ENABLED && m_reverseLimitPolarity == CANDigitalInput.LimitSwitchPolarity.kNormallyOpen){
          
            m_reverseLimitPolarity = CANDigitalInput.LimitSwitchPolarity.kNormallyClosed;
            m_reverseLimit = m_motor.getReverseLimitSwitch(m_reverseLimitPolarity);
            reverseAdvanceRequested = false;
        }

    }

// 
// Carousel FSM
//                              MODE_LOADING      MODE_UNLOADING    CAROUSEL_REQUEST_ADVANCE      LIMIT_SWITCH_POLARITY_NC   LIMIT_SWITCH_IS_ENABLED       
// 
// Carousel.Speed(0.1)                 X
// Carousel.Speed(-0.1)                                   X
// Limitswitch Set Polarity NO                                                  X                             X                         X
// Limitswtich Set Polarity NC                                                                                                          X       
// ^^ Set CAROUSEL_REQUEST_ADVANCE False                                     
// 

}