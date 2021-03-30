package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.lang.Math;

public class Autonomous extends AgencySystem {

    NetworkTable table = NetworkTableInstance.getDefault().getTable("vision");
    NetworkTableEntry bw = table.getEntry("bw");
    NetworkTableEntry bh = table.getEntry("bh");
    NetworkTableEntry bx = table.getEntry("bx");
    NetworkTableEntry by = table.getEntry("by");
    NetworkTableEntry bv = table.getEntry("bv");

    private double w;
    private double h;
    private double x;
    private double y;
    private boolean v;

    private final double TURN_THRESHHOLD =  0.1;

    private double sigmoid(double input, double stopPoint, double roughness) {
        double result = 2/(1+Math.pow((Math.E),(roughness*(stopPoint - input))))-1;
        SmartDashboard.putNumber("Turn",result);
        return result;
    }

    public double[] controller() {
        double[] ans = new double [2];
        double turnPower;
        double drivePower;

        double xCentered = 2*(x-0.5);

        //For now, don't move if there's not a ball
        if(!v) {
            ans[0] = 0.; //Change this to make it seek
            ans[1] = 0.;
        }else{
            turnPower = sigmoid(x, 0.5, 3.); 
            drivePower = sigmoid(y, 1.0, -2.0);
            System.out.print(drivePower);

            //Don't drive forward if the ball isn't centered
            //if(Math.abs(xCentered) >= TURN_THRESHHOLD) drivePower = 0.;

            ans[0] = turnPower;
            ans[1] = drivePower; 
        }
        return ans;
    }

    public void autonomousPeriodic() {
        w = bw.getDouble(0.0);
        h = bh.getDouble(0.0);
        x = bx.getDouble(0.0);
        y = by.getDouble(0.0);
        v = bv.getBoolean(false);
    }
}