package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Autonomous extends AgencySystem {

    NetworkTable table = NetworkTableInstance.getDefault().getTable("vision");
    NetworkTableEntry bw = table.getEntry("bw");
    NetworkTableEntry bh = table.getEntry("bh");
    NetworkTableEntry bx = table.getEntry("bx");
    NetworkTableEntry by = table.getEntry("by");
    NetworkTableEntry bv = table.getEntry("bv");

    public void teleopPeriodic() {
        double w = bw.getDouble(0.0);
        double h = bh.getDouble(0.0);
        double x = bx.getDouble(0.0);
        double y = by.getDouble(0.0);
        boolean v = bv.getBoolean(false);
    }
}
