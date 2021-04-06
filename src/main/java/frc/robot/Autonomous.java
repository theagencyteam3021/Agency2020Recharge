package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.SPI;

import java.lang.Math;

import com.kauailabs.navx.frc.AHRS;

public class Autonomous extends AgencySystem {

    NetworkTable table = NetworkTableInstance.getDefault().getTable("vision");
    NetworkTableEntry bx = table.getEntry("bx");
    NetworkTableEntry by = table.getEntry("by");
    NetworkTableEntry bv = table.getEntry("bv");

    //NetworkTable hTable = NetworkTableInstance.getDefault().getTable("heading");
    //NetworkTableEntry heading = hTable.getEntry("heading");

    private double heading;
    private double x;
    private double y;
    private boolean v;

    private final double TURN_THRESHHOLD =  0.45;
    private final double DISTANCE_THRESHHOLD = 0.6;

    private final double TIME_TO_DRIVE = 1.5;

    private double lastTime = -1;
    private double seekDir = 1.;

    private boolean wasValid;

    AHRS ahrs = new AHRS(SPI.Port.kMXP);

    private double sigmoid(double input, double stopPoint, double roughness) {
        double result = 2/(1+Math.pow((Math.E),(roughness*(stopPoint - input))))-1;
        SmartDashboard.putNumber("Turn",result);
        return result;
    }

    public double[] controller(int mode) {
        double[] ans = new double [3];
        double turnPower;
        double drivePower;

        double xCentered = 2*(x-0.5);

        //For now, don't move if there's not a ball
        if (mode == 0) {
            if(!v) {
                if (lastTime != -1) ans[0] = seekDir*(sigmoid(Timer.getFPGATimestamp()-lastTime, 0.2, -1.)*0.1 + 0.42);
                else ans[0] = 0.; 
                if (lastTime == -1) ans[1] = 0.3;
                else ans[1] = 0.;
                wasValid=false;
            }else{
                if (!wasValid) seekDir *= -1;
                turnPower = sigmoid(x, 0.5, 2.); 
                drivePower = sigmoid(y, 1.0, -2.0);
                System.out.print(drivePower);

                //Don't drive forward if the ball isn't centered
                if(Math.abs(xCentered) >= TURN_THRESHHOLD) drivePower = 0.;

                ans[0] = turnPower;
                ans[1] = drivePower; 

                if (y < DISTANCE_THRESHHOLD) ans[2] = 0;
                else ans[2] = 1;

                lastTime = Timer.getFPGATimestamp();
                wasValid = true;
                
            }
        } else if (mode == 1) {
            ans[0] = 0.;
            ans[1] = 0.4;
            
            if (Timer.getFPGATimestamp() - lastTime >= TIME_TO_DRIVE) ans[2] = 0;
            else ans[2] = 1;
            
        }
        else if (mode == 2) {
            if(heading <= 3 && heading >= -3) {
                ans[0] = 0.;
                ans[2] = 3.;
            } else ans[0] = sigmoid(heading,0,2)*-0.48;
        } else {
            ans[0] =0.;
            ans[1] = 0.8;
            ans[2] =3.;
        }
        return ans;
    }
    

    public void autonomousPeriodic() {
        x = bx.getDouble(0.0);
        y = by.getDouble(0.0);
        v = bv.getBoolean(false);

        heading = ahrs.getFusedHeading();

        if (heading > 180.) heading -= 360;
        SmartDashboard.putNumber("Read heading", heading);

        SmartDashboard.putData(ahrs);

        //h = heading.getDouble(-1.);
    }
}