package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

import java.lang.Math;

public class Autonomous extends AgencySystem {

    NetworkTable table = NetworkTableInstance.getDefault().getTable("vision");
    NetworkTableEntry bw = table.getEntry("bw");
    NetworkTableEntry bh = table.getEntry("bh");
    NetworkTableEntry bx = table.getEntry("bx");
    NetworkTableEntry by = table.getEntry("by");
    NetworkTableEntry bv = table.getEntry("bv");

    private double sigmoid(double input, double stopPoint, double roughness) {
        double result = 2/(Math.pow((Math.E),(roughness*((-1.0*input)+stopPoint))))-1;
        return result;
    }

    public void teleopPeriodic() {
        double w = bw.getDouble(0.0);
        double h = bh.getDouble(0.0);
        double x = bx.getDouble(0.0);
        double y = by.getDouble(0.0);
        boolean v = bv.getBoolean(false);

        double turnPower;
        if(v) {
            turnPower = sigmoid(x, 0.5, 40.0); //Change third paramater to negative if it turns opposite
        }
    }
}