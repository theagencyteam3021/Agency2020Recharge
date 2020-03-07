package frc.robot;

import frc.robot.AgencySystem;

import com.revrobotics.CANDigitalInput;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANDigitalInput.LimitSwitchPolarity;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;


public class Carousel extends AgencySystem{

    protected Boolean advanceRequested = false;
    protected Boolean reverseAdvanceRequested = false;
    protected Boolean modeLoading = true;



    private CANSparkMax m_motor;
    private CANDigitalInput m_forwardLimit;
    private CANDigitalInput m_reverseLimit;
    private CANDigitalInput.LimitSwitchPolarity m_forwardLimitPolarity;
    private CANDigitalInput.LimitSwitchPolarity m_reverseLimitPolarity;


    //Limit Switch for Ball Position
    // DI -> Port       Ball Position
    //          1               1
    //          2               2
    //          3               3


//     +---------+
//     |         |
//     |    3    |
//     |         |
//     |         |
//     +---------+

// +----------+            +----------+
// |          |            |          |
// |          |            |          |
// |    1     |            |    2     |
// |          |            |          |
// |          |            |          |
// +----------+            +----------+

    private DigitalInput beam1;
    private DigitalInput beam2;
    private DigitalInput beam3;


    /*
    public Carousel(int deviceID, Boolean debug){
        new Carousel(deviceID, "CAN" + String.valueOf(deviceID), false);
    }
    public Carousel(int deviceID, String Name){
        new Carousel(deviceID, Name, false);
    }
*/
    public Carousel(int deviceID, int beam1, int beam2, int beam3, String name, Boolean debug){
    
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

    m_forwardLimitPolarity = CANDigitalInput.LimitSwitchPolarity.kNormallyClosed;
    m_forwardLimit = new CANDigitalInput(m_motor,CANDigitalInput.LimitSwitch.kForward, m_forwardLimitPolarity);
    m_forwardLimit = m_motor.getForwardLimitSwitch(m_forwardLimitPolarity);
    
    
    m_reverseLimitPolarity = CANDigitalInput.LimitSwitchPolarity.kNormallyOpen;
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
    m_forwardLimit.enableLimitSwitch(true);
    m_reverseLimit.enableLimitSwitch(true);

    m_motor.setIdleMode(IdleMode.kBrake);

    //digital inputs
    this.beam1 = new DigitalInput(beam1);
    this.beam2 = new DigitalInput(beam2);
    this.beam3 = new DigitalInput(beam3);

    }

    public Boolean hasBall1() {
        console_debug("Has Ball 1 -- " + !beam1.get());
        return !beam1.get();
    }

    public Boolean hasBall2() {
        console_debug("Has Ball 2 -- " + !beam2.get());
        return !beam2.get();
    }

    public Boolean hasBall3() {
        console_debug("Has Ball 3 -- "+ !beam3.get());
        return !beam3.get();
    }

    public void requestForwardAdvance() {
        advanceRequested = true;
        reverseAdvanceRequested = false;
    }

    public void requestReverseAdvance() {
        reverseAdvanceRequested = true;
        advanceRequested = false;
    }

    /*public void loadBalls(){
        modeLoading = true;
    }

    public void unloadBalls(){
        modeLoading = false;
    }*/

    public boolean isRotating(){
        return !m_forwardLimit.get();
    }

    public void teleopInit() {
        this.advanceRequested = true;
    }
    public void teleopPeriodic() {
        boolean FORWARD_LIMIT_ENABLED = m_forwardLimit.get();
        boolean REVERSE_LIMIT_ENABLED = m_reverseLimit.get();

        if (advanceRequested){
            m_motor.set(0.15);
        }
        else if (reverseAdvanceRequested){
            m_motor.set(-0.15);
        }
    
        console_debug("Carousel Request " + advanceRequested);
        console_debug("Polarity " + m_forwardLimitPolarity);
        console_debug("Limit Switch Enabled " + FORWARD_LIMIT_ENABLED);

        if (advanceRequested && m_forwardLimitPolarity == CANDigitalInput.LimitSwitchPolarity.kNormallyOpen && FORWARD_LIMIT_ENABLED){
            m_forwardLimitPolarity = CANDigitalInput.LimitSwitchPolarity.kNormallyClosed;
            m_forwardLimit = m_motor.getForwardLimitSwitch(m_forwardLimitPolarity);  
        }
        
        else if(FORWARD_LIMIT_ENABLED && m_forwardLimitPolarity == CANDigitalInput.LimitSwitchPolarity.kNormallyOpen){
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