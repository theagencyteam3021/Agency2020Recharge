package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.lang.Math;

public class Autonomous extends AgencySystem {

    NetworkTable table = NetworkTableInstance.getDefault().getTable("vision");
    NetworkTableEntry bw = table.getEntry("bw");
    NetworkTableEntry bh = table.getEntry("bh");
    NetworkTableEntry bx = table.getEntry("bx");
    NetworkTableEntry by = table.getEntry("by");
    NetworkTableEntry bv = table.getEntry("bv");

    NetworkTable hTable = NetworkTableInstance.getDefault().getTable("heading");
    NetworkTableEntry heading = hTable.getEntry("heading");

    private double w;
    private double h;
    private double x;
    private double y;
    private boolean v;

    private final double TURN_THRESHHOLD =  0.4;
    private final double DISTANCE_THRESHHOLD = 0.7;

    private final double TIME_TO_DRIVE = 1.5;

    private double lastTime = -1;

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
                if (Timer.getFPGATimestamp()-lastTime > 0.5 && lastTime != -1) ans[0] = sigmoid(Timer.getFPGATimestamp()-lastTime, 0.2, -10.)*0.1 + 0.45;
                else ans[0] = 0.; 
                if (lastTime == -1) ans[1] = 0.3;
                else ans[1] = 0.;
            }else{
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
                
            }
        } else if (mode == 1) {
            ans[0] = 0.;
            ans[1] = 0.4;
            
            if (Timer.getFPGATimestamp() - lastTime >= TIME_TO_DRIVE) ans[2] = 0;
            else ans[2] = 1;
            
        }
        else {
            if(h < 30 && h > 0) {
                //ans[1] = 0.35;
                ans[0] = 0.;
            } else ans[0] = -0.4;
        }
        return ans;
    }
    

    public void autonomousPeriodic() {
        w = bw.getDouble(0.0);
        h = bh.getDouble(0.0);
        x = bx.getDouble(0.0);
        y = by.getDouble(0.0);
        v = bv.getBoolean(false);

        h = heading.getDouble(-1.);
    }
}