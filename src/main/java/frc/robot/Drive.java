package frc.robot;

import frc.robot.AgencySystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;


public class Drive extends AgencySystem {

    private DifferentialDrive drive;

    private CANSparkMax leftMotor, left2motor;
    private CANSparkMax rightMotor, right2motor;

    private CANEncoder mLeft_encoder;
    private CANEncoder mRight_encoder;

    //The diameter of the drive wheels is 4.825 inches

    private static double CIRC = ( (Math.PI)  * 4.825);

    public Drive(int frontLeftID, int frontRightID, int backLeftID, int backRight, String name, Boolean debug) {

        this.name = name;
        this.debug = debug;

        leftMotor = new CANSparkMax(frontLeftID, MotorType.kBrushless);
        left2motor = new CANSparkMax(backLeftID, MotorType.kBrushless);
        rightMotor = new CANSparkMax(frontRightID, MotorType.kBrushless);
        right2motor = new CANSparkMax(backRight, MotorType.kBrushless);

        leftMotor.restoreFactoryDefaults();
        left2motor.restoreFactoryDefaults();

        rightMotor.restoreFactoryDefaults();
        right2motor.restoreFactoryDefaults();


        left2motor.follow(leftMotor);
        right2motor.follow(rightMotor);

        drive = new DifferentialDrive(leftMotor, rightMotor);

        mLeft_encoder = new CANEncoder(leftMotor);
        mRight_encoder = new CANEncoder(rightMotor);

        mLeft_encoder.setPositionConversionFactor(42);
        mRight_encoder.setPositionConversionFactor(42);

        rightMotor.burnFlash();
        right2motor.burnFlash();
        left2motor.burnFlash();
        leftMotor.burnFlash();
    }

    public void drive(double speed, double rotation){
        drive.arcadeDrive(speed,rotation);  //(-ypos,xpos)

        
    }


/*Distance traveled = (Encoder ticks / 360) * circumference

which leads up to our final, useful formula:

Encoder ticks = (360 / circumference) * Distance to travel

*/
    // public void driveAutoForward ( double distanceInInches){
    //     double encoderTicks = ( (360/ CIRC) * distanceInInches);
    //     double rotations = encoderTicks / 42 ;

    //     mLeft_encoder.setPosition(rotations);
    //     mRight_encoder.setPosition(rotations);
    //     System.out.println("Distance traveled = " + ((mLeft_encoder.getPosition() / 360) * CIRC));

    // }

    // public void driveAutoBackward ( double distanceInInches){
    //     double encoderTicks = ( (360/ CIRC) * distanceInInches);
    //     double rotations = encoderTicks / 42 ;

    //     mLeft_encoder.setPosition(rotations);
    //     mRight_encoder.setPosition(rotations);
    //     System.out.println("Distance traveled = " + ((mLeft_encoder.getPosition() / 360) * CIRC));

    // }

    // public void turnDegrees(double degrees){
    //         //TODO: Implement NavX
    // }

    public void teleopPeriodic() {

    }
}