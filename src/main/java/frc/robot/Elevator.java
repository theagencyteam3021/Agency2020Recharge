package frc.robot;

import frc.robot.AgencySystem;

import com.revrobotics.CANDigitalInput;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;


public class Elevator extends AgencySystem{
    protected Boolean outtakeRequested = false;
    protected Boolean elevateRequested = false;

    private CANSparkMax m_outtake;
    private CANSparkMax m_elevator;
    private CANDigitalInput m_forwardLimitElevator;
    private CANDigitalInput.LimitSwitchPolarity m_forwardLimitElevatorPolarity;

    private DigitalInput elevatorBeamSensor = new DigitalInput(4);

    public Elevator(int deviceID1, int deviceID2, String name, Boolean debug){
    
        this.name = name;
        this.debug = debug;

        m_outtake = new CANSparkMax(deviceID1, MotorType.kBrushless);
        m_elevator = new CANSparkMax(deviceID2, MotorType.kBrushless);


        m_outtake.restoreFactoryDefaults();
        m_elevator.restoreFactoryDefaults();

        m_forwardLimitElevatorPolarity = CANDigitalInput.LimitSwitchPolarity.kNormallyOpen;
        m_forwardLimitElevator = m_elevator.getForwardLimitSwitch(m_forwardLimitElevatorPolarity);

        m_forwardLimitElevator.enableLimitSwitch(false);

        m_elevator.set(.5);
    
    }

    public Boolean hasBall() {
        return elevatorBeamSensor.get();
    }

    public Boolean isDown() {
        return m_forwardLimitElevator.get();
    }

    public void requestOuttake() {
        outtakeRequested = true;
    }

    public void requestElevate() {
        elevateRequested = true;        
    }

    public void teleopPeriodic() {

        if (outtakeRequested && !this.hasBall() && this.isDown()) {
            m_outtake.set(.5);
        }
        else {
            m_outtake.set(0);
            this.outtakeRequested = false;
        }

        if (elevateRequested && m_forwardLimitElevatorPolarity == CANDigitalInput.LimitSwitchPolarity.kNormallyClosed && this.isDown()){
            m_forwardLimitElevatorPolarity = CANDigitalInput.LimitSwitchPolarity.kNormallyOpen;
            m_forwardLimitElevator = m_elevator.getForwardLimitSwitch(m_forwardLimitElevatorPolarity);
          
    
        }
        else if(this.isDown() && m_forwardLimitElevatorPolarity == CANDigitalInput.LimitSwitchPolarity.kNormallyOpen){
          
            m_forwardLimitElevatorPolarity = CANDigitalInput.LimitSwitchPolarity.kNormallyClosed;
            m_forwardLimitElevator = m_elevator.getForwardLimitSwitch(m_forwardLimitElevatorPolarity);
            this.elevateRequested = false;
        }
    }

}
