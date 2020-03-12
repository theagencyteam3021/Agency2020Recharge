package frc.robot;

import frc.robot.AgencySystem;

import com.revrobotics.CANDigitalInput;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANDigitalInput.LimitSwitchPolarity;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.CANEncoder;


public class Intake extends AgencySystem{
    private CANDigitalInput m_forwardLimit1;
    private CANDigitalInput m_forwardLimit2;
    private CANDigitalInput.LimitSwitchPolarity m_forwardLimitPolarity;
    protected Boolean advanceRequested = false;
    private Boolean intakeRequested = false;

    // private Boolean reverseAdvanceRequest = false;
    // private Boolean reverseIntakeRequest = false;

    private Boolean oneInTheChute = false;
    private CANSparkMax m_stage1, m_stage2;
    private CANEncoder m_stage2_encoder;

    double stage1Power = 0.5;
    double stage2Power = 0.8;

    public Intake(int deviceID1, int deviceID2, String name, Boolean debug){
    
        this.name = name;
        this.debug = debug;

        m_stage1 = new CANSparkMax(deviceID1, MotorType.kBrushless);
        m_stage2 = new CANSparkMax(deviceID2, MotorType.kBrushless);

        m_stage1.restoreFactoryDefaults();
        m_stage2.restoreFactoryDefaults();

        m_stage1.setInverted(true);
        m_stage2.setInverted(false);

        m_forwardLimitPolarity = CANDigitalInput.LimitSwitchPolarity.kNormallyOpen;
        m_forwardLimit1 = m_stage1.getForwardLimitSwitch(LimitSwitchPolarity.kNormallyOpen);
        m_forwardLimit2 = m_stage2.getForwardLimitSwitch(m_forwardLimitPolarity);

        m_forwardLimit1.enableLimitSwitch(true);
        m_forwardLimit2.enableLimitSwitch(true);

        m_stage2.setIdleMode(IdleMode.kBrake);

        // m_stage2_encoder = m_stage2.getEncoder();


        m_stage1.burnFlash();
        m_stage2.burnFlash();
    }

   
    public void requestIntake() {
        console_debug("requestIntake");
        intakeRequested = true;
        m_stage1.set(stage1Power); //-0.75 --
        m_stage2.set(stage2Power); //0.5
    }

    public void requestStop() {
        console_debug("requestStop");
        intakeRequested = false;
        m_stage1.set(0);
        m_stage2.set(0);
    }
    public boolean inProgress(){
        return advanceRequested;
    }
    public Boolean hasBall() {
        Boolean r = false;
        // m_stage2.getForwardLimitSwitch(m_forwardLimitPolarity).get( 
        if ( oneInTheChute){
            r =  true;
        }
        

        //  if (m_forwardLimitPolarity == CANDigitalInput.LimitSwitchPolarity.kNormallyClosed 
        //         && !m_stage2.getForwardLimitSwitch(m_forwardLimitPolarity).get()){
        //     r =  true;
        // }
        // else if (m_forwardLimitPolarity == CANDigitalInput.LimitSwitchPolarity.kNormallyOpen 
        // && m_stage2.getForwardLimitSwitch(m_forwardLimitPolarity).get()){
        //     r =  true;
        // }
        console_debug("hasBall: " + r);
        return r; 
    }

    //advance only if there is a ball, separate the speed control groups
    //maybe make them both have the same forward limit switch instead of speed control groups

    public void requestAdvance() {
        advanceRequested = true;
    }

    ///Controls for Secondary 

// /////////////    
//     public void requestReverseAdvance(){
//         reverseAdvanceRequest = true;
//     }
//     public void requesetReverseIntake(){
//         reverseIntakeRequest = true;
//     }

    public void reverseAdvance(){
        m_stage2.set(-stage2Power);
    }

    public void reverseIntake(){
        console_debug("REVERSE Intake Requested");
        //intakeRequested = true;
        m_stage1.set(-stage1Power); 
       
    }
////////////
    public void teleopPeriodic() {

        if(intakeRequested){
            console_debug("Intake Requested");

        }else{
            console_debug("Intake Not Requested"); 
        }

        // if (m_stage2_encoder.getVelocity() == 0){
        //     m_stage1.set(0);
        // }

        if (m_forwardLimitPolarity == CANDigitalInput.LimitSwitchPolarity.kNormallyOpen && m_forwardLimit2.get())
        {
            oneInTheChute = true;
        }
        else {
            oneInTheChute = false;
        }
        if (advanceRequested && m_forwardLimitPolarity == CANDigitalInput.LimitSwitchPolarity.kNormallyOpen && m_forwardLimit2.get()){
            console_debug("Forward Limit Switch Polarity NO , Switching to NC");
            m_forwardLimitPolarity = CANDigitalInput.LimitSwitchPolarity.kNormallyClosed;
            m_forwardLimit2 = m_stage2.getForwardLimitSwitch(m_forwardLimitPolarity);
        }
        else if(m_forwardLimitPolarity == CANDigitalInput.LimitSwitchPolarity.kNormallyClosed && m_forwardLimit2.get()){
            console_debug("Forward Limit Switch Polarity NC , Switching to NO");
            m_forwardLimitPolarity = CANDigitalInput.LimitSwitchPolarity.kNormallyOpen;
            m_forwardLimit2 = m_stage2.getForwardLimitSwitch(m_forwardLimitPolarity);
            advanceRequested = false;
        }
        

       


    }
}