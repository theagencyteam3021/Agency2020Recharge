package frc.robot;

import frc.robot.AgencySystem;

import com.revrobotics.CANDigitalInput;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANDigitalInput.LimitSwitchPolarity;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;


public class Intake extends AgencySystem{
    private CANDigitalInput m_forwardLimit1;
    private CANDigitalInput m_forwardLimit2;
    private CANDigitalInput.LimitSwitchPolarity m_forwardLimitPolarity;
    protected Boolean advanceRequested = false;
    private Boolean intakeRequested = false;
    private CANSparkMax m_stage1, m_stage2;
    public Intake(int deviceID1, int deviceID2, String name, Boolean debug){
    
        this.name = name;
        this.debug = debug;

        m_stage1 = new CANSparkMax(deviceID1, MotorType.kBrushless);
        m_stage2 = new CANSparkMax(deviceID2, MotorType.kBrushless);

        m_stage1.restoreFactoryDefaults();
        m_stage2.restoreFactoryDefaults();

        m_forwardLimitPolarity = CANDigitalInput.LimitSwitchPolarity.kNormallyOpen;
        m_forwardLimit1 = m_stage1.getForwardLimitSwitch(m_forwardLimitPolarity);
        m_forwardLimit2 = m_stage2.getForwardLimitSwitch(LimitSwitchPolarity.kNormallyClosed);

        m_forwardLimit1.enableLimitSwitch(true);
        m_forwardLimit2.enableLimitSwitch(true);
    }


    public void requestIntake() {
        intakeRequested = true;
    }

    public void requestStop() {
        intakeRequested = false;
    }

    public Boolean hasBall() {
        //TODO: implement limit switch 
        return false;
    }

    //advance only if there is a ball, separate the speed control groups
    //maybe make them both have the same forward limit switch instead of speed control groups

    public void requestAdvance() {
        advanceRequested = true;
    }

    public void teleopPeriodic() {

        if(intakeRequested){
            m_stage1.set(.5);
            m_stage2.set(.5);

        }else{
            m_stage1.set(0);
            m_stage2.set(0);
        }

        if (advanceRequested && m_forwardLimitPolarity == CANDigitalInput.LimitSwitchPolarity.kNormallyClosed && m_forwardLimit2.get()){
            m_forwardLimitPolarity = CANDigitalInput.LimitSwitchPolarity.kNormallyOpen;
            m_forwardLimit2 = m_stage2.getForwardLimitSwitch(m_forwardLimitPolarity);
        }
        else if(m_forwardLimit2.get() && m_forwardLimitPolarity == CANDigitalInput.LimitSwitchPolarity.kNormallyOpen){
            m_forwardLimitPolarity = CANDigitalInput.LimitSwitchPolarity.kNormallyClosed;
            m_forwardLimit2 = m_stage2.getForwardLimitSwitch(m_forwardLimitPolarity);
            advanceRequested = false;
        }


    }
}