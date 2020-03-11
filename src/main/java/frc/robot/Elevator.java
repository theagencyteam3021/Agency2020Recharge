package frc.robot;

import frc.robot.AgencySystem;

import com.revrobotics.CANDigitalInput;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;


public class Elevator extends AgencySystem{
    protected Boolean outtakeRequested = false;
    protected Boolean elevateRequested = false;

    private CANSparkMax m_outtake;
    private CANSparkMax m_elevator;

    private CANEncoder m_elevatorEncoder;

    private CANDigitalInput m_forwardLimitElevator;
    private CANDigitalInput.LimitSwitchPolarity m_forwardLimitElevatorPolarity;

    private DigitalInput elevatorBeamSensor;

    private float elevatorSoftLimit = 0;

    private double elevatorStartingPos = 0;

    

    public Elevator(int deviceID1, int deviceID2, int beam, String name, Boolean debug){
    
        this.name = name;
        this.debug = debug;

        m_outtake = new CANSparkMax(deviceID1, MotorType.kBrushless);
        m_elevator = new CANSparkMax(deviceID2, MotorType.kBrushless);


        m_outtake.restoreFactoryDefaults();
        m_elevator.restoreFactoryDefaults();

        m_forwardLimitElevatorPolarity = CANDigitalInput.LimitSwitchPolarity.kNormallyOpen;
        m_forwardLimitElevator = m_elevator.getForwardLimitSwitch(m_forwardLimitElevatorPolarity);

        m_forwardLimitElevator.enableLimitSwitch(true);

        m_elevator.setIdleMode(IdleMode.kBrake);
        m_outtake.setIdleMode(IdleMode.kBrake);

        m_elevator.burnFlash();
        m_outtake.burnFlash();

        m_elevatorEncoder = m_elevator.getEncoder();

        this.elevatorBeamSensor = new DigitalInput(beam);
    
    }

    public Boolean hasBall() {
        console_debug("Has Ball " + !elevatorBeamSensor.get());
        return !elevatorBeamSensor.get();
    }

    public Boolean isDown() {
        //console_debug("Is Down " + m_forwardLimitElevator.get());
       // System.out.println("vel in isdown: " + m_elevatorEncoder.getVelocity());
        if (m_elevatorEncoder.getVelocity() == 0 ) return true;
        return false;
       // return m_forwardLimitElevator.get();
    }



    public void requestOuttake() {
        outtakeRequested = true;
    }

    public boolean outtakeInProgress(){
        return outtakeRequested;
    }

    public void requestElevate() {
        if (!elevateRequested) {
            elevateRequested = true;
          //  elevatorSoftLimit = (float) m_elevator.getSoftLimit(SoftLimitDirection.kForward) + 90;
            m_elevator.setSoftLimit(SoftLimitDirection.kForward, 90);
           // m_forwardLimitElevator.enableLimitSwitch(false);
        }
    }

    public void teleopInit() {
        m_elevator.set(.1);
        elevatorStartingPos = m_elevatorEncoder.getPosition();
    }

    public void teleopPeriodic() {

        if (outtakeRequested && !this.hasBall() && this.isDown()) {
            m_outtake.set(1.0);
        }
        else {
            m_outtake.set(0);
            this.outtakeRequested = false;
        }

        console_debug("Soft Limit Value - > "+ (m_elevator.getSoftLimit(SoftLimitDirection.kForward)));
        console_debug("Eleveator Soft Limit - > " + elevatorSoftLimit);

    
        if (m_elevatorEncoder.getPosition() >= m_elevator.getSoftLimit(SoftLimitDirection.kForward)){
            m_elevator.set(0.1);
            
            m_elevator.enableSoftLimit(SoftLimitDirection.kForward, false);
            m_forwardLimitElevatorPolarity = CANDigitalInput.LimitSwitchPolarity.kNormallyOpen;
            m_forwardLimitElevator = m_elevator.getForwardLimitSwitch(m_forwardLimitElevatorPolarity);
        
        }
        if (elevateRequested && m_forwardLimitElevatorPolarity == CANDigitalInput.LimitSwitchPolarity.kNormallyOpen ){ //&& this.isDown()
            m_elevator.set(0.75);//0.6
            m_forwardLimitElevatorPolarity = CANDigitalInput.LimitSwitchPolarity.kNormallyClosed;
            m_forwardLimitElevator = m_elevator.getForwardLimitSwitch(m_forwardLimitElevatorPolarity);
            m_elevator.enableSoftLimit(SoftLimitDirection.kForward, true);
            m_elevator.setSoftLimit(SoftLimitDirection.kForward, (float)(m_elevatorEncoder.getPosition() + 75.0)); //50
            
           

    
        }
        else if( m_forwardLimitElevator.get() && m_forwardLimitElevatorPolarity == CANDigitalInput.LimitSwitchPolarity.kNormallyClosed){
            m_elevator.set(0.75);
            m_forwardLimitElevatorPolarity = CANDigitalInput.LimitSwitchPolarity.kNormallyOpen;
            m_forwardLimitElevator = m_elevator.getForwardLimitSwitch(m_forwardLimitElevatorPolarity);
            this.elevateRequested = false;
           
        }
    }

    

}
