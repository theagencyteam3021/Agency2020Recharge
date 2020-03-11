package frc.robot;

import frc.robot.AgencySystem;

import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;

public class Shooter extends AgencySystem{
    protected Boolean shotRequested = false;
    public enum ShooterAngle{SHORT,LONG};

    private CANSparkMax m_motorL;
    private CANSparkMax m_motorR;
   // private CANSparkMax m_angle;
   private CANPIDController m_L_pidController;
   private CANPIDController m_R_pidController;
   private CANEncoder m_L_encoder;
   private CANEncoder m_R_encoder;
   public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, maxRPM, maxVel, minVel, maxAcc, allowedErr;
 

    public Shooter(int leftMotor, int rightMotor, int angleID, String name,
            Boolean debug) {

    this.name = name;
    this.debug = debug;

    m_motorL = new CANSparkMax(leftMotor, MotorType.kBrushless);
    m_motorR = new CANSparkMax(rightMotor, MotorType.kBrushless);
    //   m_angle = new CANSparkMax(angleID, MotorType.kBrushless);

    m_motorL.restoreFactoryDefaults();
    m_motorR.restoreFactoryDefaults();
    //  m_angle.restoreFactoryDefaults();

    m_motorR.setInverted(true);
 
    // initialze PID controller and encoder objects
    m_L_pidController = m_motorL.getPIDController();
    m_L_encoder = m_motorL.getEncoder();

    m_R_pidController = m_motorR.getPIDController();
    m_R_encoder = m_motorR.getEncoder();

    // PID coefficients
    kP = 5e-5; 
    kI = 1e-6;
    kD = 0; 
    kIz = 0; 
    kFF = 0.000156; 
    kMaxOutput = 1; 
    kMinOutput = -1;
    maxRPM = 4500; //3000 //1500// THIS IS THE SHOT SPEED

    // Smart Motion Coefficients
    maxVel = 4500; //3000 //1500 // rpm
    maxAcc = 1000;

    // set PID coefficients
    m_L_pidController.setP(kP);
    m_L_pidController.setI(kI);
    m_L_pidController.setD(kD);
    m_L_pidController.setIZone(kIz);
    m_L_pidController.setFF(kFF);
    m_L_pidController.setOutputRange(kMinOutput, kMaxOutput);

    m_R_pidController.setP(kP);
    m_R_pidController.setI(kI);
    m_R_pidController.setD(kD);
    m_R_pidController.setIZone(kIz);
    m_R_pidController.setFF(kFF);
    m_R_pidController.setOutputRange(kMinOutput, kMaxOutput);

   /**
     * Smart Motion coefficients are set on a CANPIDController object
     * 
     * - setSmartMotionMaxVelocity() will limit the velocity in RPM of
     * the pid controller in Smart Motion mode
     * - setSmartMotionMinOutputVelocity() will put a lower bound in
     * RPM of the pid controller in Smart Motion mode
     * - setSmartMotionMaxAccel() will limit the acceleration in RPM^2
     * of the pid controller in Smart Motion mode
     * - setSmartMotionAllowedClosedLoopError() will set the max allowed
     * error for the pid controller in Smart Motion mode
     */
    int smartMotionSlot = 0;
    m_L_pidController.setSmartMotionMaxVelocity(maxVel, smartMotionSlot);
    m_L_pidController.setSmartMotionMinOutputVelocity(minVel, smartMotionSlot);
    m_L_pidController.setSmartMotionMaxAccel(maxAcc, smartMotionSlot);
    m_L_pidController.setSmartMotionAllowedClosedLoopError(allowedErr, smartMotionSlot);

    m_R_pidController.setSmartMotionMaxVelocity(maxVel, smartMotionSlot);
    m_R_pidController.setSmartMotionMinOutputVelocity(minVel, smartMotionSlot);
    m_R_pidController.setSmartMotionMaxAccel(maxAcc, smartMotionSlot);
    m_R_pidController.setSmartMotionAllowedClosedLoopError(allowedErr, smartMotionSlot);

    m_motorL.burnFlash();
    m_motorR.burnFlash();

    }

    public void requestStart() {

        this.shotRequested = true;

    }

    public void requestStop() {
        this.shotRequested = false;
    }

    public void requestAngleChange(ShooterAngle requestedAngle) {

        //???? por que = requestedAngle;
        
    }

    public void teleopPeriodic() {        
        if (shotRequested) {
            m_L_pidController.setReference(maxRPM, ControlType.kVelocity);
            m_R_pidController.setReference(maxRPM, ControlType.kVelocity);
        }
        else {
            // m_L_pidController.setReference(0, ControlType.kVelocity);
            // m_R_pidController.setReference(0, ControlType.kVelocity);          
            
            m_motorL.disable();
            m_motorR.disable();

           // m_motorL.set(0);
           // m_motorR.set(0);
           // m_pidController.setReference(0, ControlType.kVelocity);
        }
        

    }

    
}