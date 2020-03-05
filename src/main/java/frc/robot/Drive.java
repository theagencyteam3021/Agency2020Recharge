package frc.robot;

import frc.robot.AgencySystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;


public class Drive extends AgencySystem {

    private DifferentialDrive drive;

    private CANSparkMax leftMotor, left2motor;
    private CANSparkMax rightMotor, right2motor;

    public Drive(int frontLeftID, int frontRightID, int backLeftID, int backRight, String name, Boolean debug) {

        this.name = name;
        this.debug = debug;

        leftMotor = new CANSparkMax(frontLeftID, MotorType.kBrushless);
        left2motor = new CANSparkMax(backLeftID, MotorType.kBrushless);
        rightMotor = new CANSparkMax(frontRightID, MotorType.kBrushless);
        right2motor = new CANSparkMax(backRight, MotorType.kBrushless);

        left2motor.follow(leftMotor);
        right2motor.follow(rightMotor);

        drive = new DifferentialDrive(leftMotor, rightMotor);

    }

    public void teleopPeriodic() {

    }
}