package frc.robot;

import frc.robot.AgencySystem;
// import edu.wpi.first.wpilibj.drive.DifferentialDrive;
// import com.revrobotics.CANSparkMax;
// import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
// import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;


public class Climber extends AgencySystem {

    private WPI_TalonFX  talonFX;

    

    

    public Climber(int talonFXID, String name, Boolean debug) {

        this.name = name;
        this.debug = debug;

        talonFX = new WPI_TalonFX(talonFXID);
        talonFX.configFactoryDefault();
        talonFX.setNeutralMode(NeutralMode.Brake);

         talonFX.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen);
         talonFX.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen);

    }

    
    public void raiseClimber(){
        talonFX.set(-0.3);
    }

    public void lowerClimber(){
        talonFX.set(0.6);
    }

    public void stopClimber(){
        talonFX.set(0);
    }


    public void teleopPeriodic() {

    }
}