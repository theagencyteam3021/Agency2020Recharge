package frc.robot;

import frc.robot.AgencySystem;

public class BallHandler extends AgencySystem {

    private Intake intake;
    private Carousel carousel;
    private Elevator elevator;
    private Shooter shooter;

    private enum ballHandlerMode {
        LOADING, SHOOTING
    };

    private ballHandlerMode lastMode;
    private boolean justLoaded = false;
    private boolean intakeRequested = false;
    private boolean shotRequested = false;

    public BallHandler(String name, Boolean debug) {

        this.name = name;
        this.debug = debug;

        this.intake = new Intake(RobotMap.intakeStage1, RobotMap.intakeStage2, "intake", debug);
        this.carousel = new Carousel(RobotMap.carousel, RobotMap.carouselBeam1, RobotMap.carouselBeam2,
                RobotMap.carouselBeam3, "carousel", debug);
        this.elevator = new Elevator(RobotMap.outtake, RobotMap.elevator, RobotMap.elevatorBeam, "elevator", debug);
        this.shooter = new Shooter(RobotMap.lShooter, RobotMap.rShooter, RobotMap.shooterAngle, "shooter", debug);

    }

    public void startLoad() {
        console_debug("Start Load");
        this.lastMode = ballHandlerMode.LOADING;
        this.intakeRequested = true;
        this.shotRequested = false;
        this.intake.requestIntake();
    }

    public void stopLoad() {
        intakeRequested = false;
        this.intake.requestStop();
    }

    public void shoot() {
        this.lastMode = ballHandlerMode.SHOOTING;
        this.shotRequested = true;
        this.intakeRequested = false;

    }

    public void changeShootAngle(Shooter.ShooterAngle angle) {
        shooter.requestAngleChange(angle);
    }

    public void teleopInit() {
        lastMode = ballHandlerMode.LOADING;
        carousel.teleopInit();
        elevator.teleopInit();

    }

    public void teleopPeriodicLogic() {
        // console_debug("teleop");
        // if(elevator.isDown() && !elevator.hasBall()){
        // shooter.requestStop();
        // //if(carousel.hasBall3()){
        // // elevator.requestOuttake();
        // //}
        // }

        // if (justLoaded) {
        // console_debug("Loading 0");
        // carousel.requestReverseAdvance();
        // justLoaded = false;
        // return;
        // }

        // if (carousel.isRotating()) {
        // console_debug("bailing because carousel reports rotating");
        // return;
        // }

        // if (elevator.outtakeInProgress()) {
        // console_debug("Outtake in Progress");
        // return;
        // }

        if (elevator.isDown()) {
            shooter.requestStop();
        }

        if (shotRequested && elevator.hasBall()) {
            shooter.requestStart();
            elevator.requestElevate();
            shotRequested = false;
            return;
        }

        if (carousel.isRotating() ) { //&& intake.hasBall()
            console_debug("Carousel Is Rotating RETURNING");
            intake.requestStop();
            return;
        }

        // if (elevator.outtakeInProgress()){
        // return;
        // }

        if (intake.inProgress()) {
            console_debug("Intake in Progress RETURNING");
            return;
        }

        if (intake.hasBall() && !carousel.hasBall1()) {
            console_debug("if(intake.hasBall() && !carousel.hasBall1())");
            intake.requestAdvance();
            justLoaded = true;
            return;
        }

        // if (intake.inProgress()){
        // console_debug("Intake in Progress RETURNING");
        // return;
        // }

        if (justLoaded && carousel.hasBall1()) {
            console_debug("if (justLoaded && carousel.hasBall1() )");
            justLoaded = false;
            carousel.requestReverseAdvance();
            return;
        }
        if (elevator.isDown() && !elevator.hasBall() && carousel.hasBall3()) {
            console_debug("if (elevator.isDown() && !elevator.hasBall() && carousel.hasBall3())");
            elevator.requestOuttake();
            carousel.requestForwardAdvance();
            return;
        }
        if (carousel.hasBall2() && !carousel.hasBall3()) {
            console_debug("if (carousel.hasBall2() && !carousel.hasBall3())");
            carousel.requestReverseAdvance();
            return;
        }
        if (carousel.hasBall1() && !carousel.hasBall3()) {
            console_debug("if (carousel.hasBall1() && !carousel.hasBall3())");
            carousel.requestForwardAdvance();
            return;
        }
        if (carousel.hasBall1() && !carousel.hasBall2()) {
            console_debug("if (carousel.hasBall1() && !carousel.hasBall2())");
            carousel.requestForwardAdvance();
            return;

        }

        console_debug("Reached the End of the Line");
        // if (!elevator.hasBall() && elevator.isDown()){
        // carou
        // }

        // // if(lastMode == ballHandlerMode.LOADING){
        // if (elevator.isDown() && !elevator.hasBall() && carousel.hasBall3()) {
        // console_debug("Loading 1");
        // elevator.requestOuttake();
        // carousel.requestForwardAdvance();
        // } else if (intake.hasBall() && carousel.hasBall1() && !carousel.hasBall2()) {
        // console_debug("Loading 2");
        // carousel.requestReverseAdvance();
        // }
        // /*
        // * else if(elevator.hasBall() && intake.hasBall() && !carousel.hasBall3()){
        // * console_debug("Loading 3"); carousel.requestReverseAdvance(); }
        // */
        // else if ((carousel.hasBall1() && !carousel.hasBall3())) {
        // console_debug("Loading 4");
        // carousel.requestReverseAdvance();
        // } else if ((carousel.hasBall2()) && !carousel.hasBall3()) {
        // console_debug("Loading 4.5");
        // carousel.requestReverseAdvance();
        // } else if (intake.hasBall() && !carousel.hasBall1()) {
        // console_debug("Loading 5");
        // intake.requestAdvance();
        // justLoaded = true;
        // } else if (elevator.hasBall() && carousel.hasBall1() && carousel.hasBall3()
        // && !carousel.hasBall2()) {
        // carousel.requestReverseAdvance();
        // } else {
        // console_debug("Lost");
        // }

        // if (elevator.isDown()) {
        // shooter.requestStop();
        // }

        // if (shotRequested && elevator.hasBall()) {
        // shooter.requestStart();
        // elevator.requestElevate();
        // shotRequested = false;
        // }

        // }

        // else if(lastMode == ballHandlerMode.SHOOTING){

        // if(shotRequested && elevator.hasBall()){
        // shooter.requestStart();
        // elevator.requestElevate();
        // shotRequested = false;
        // }
        // if(elevator.isDown() && !elevator.hasBall() && carousel.hasBall3()){
        // elevator.requestOuttake();
        // }
        // /* else if(elevator.hasBall() && intake.hasBall() && !carousel.hasBall1()){
        // intake.requestAdvance();
        // }*/
        // else if(elevator.hasBall() && intake.hasBall() && !carousel.hasBall2()){
        // carousel.requestForwardAdvance();
        // }
        // else if(elevator.hasBall() && intake.hasBall() && !carousel.hasBall3()){
        // carousel.requestReverseAdvance();
        // }
        // else if((carousel.hasBall1() || carousel.hasBall2()) &&
        // !carousel.hasBall3()){
        // carousel.requestReverseAdvance();
        // }
        // else if(intake.hasBall() && !carousel.hasBall1()){
        // intake.requestAdvance();
        // justLoaded = true;
        // }
        // }
    }

    public void teleopPeriodic() {
        console_debug("teleop");
        teleopPeriodicLogic();

        /*
         * try { Thread.sleep(15000); } catch (InterruptedException e) { // TODO
         * Auto-generated catch block Thread.currentThread().interrupt(); }
         */

        this.intake.teleopPeriodic();
        this.carousel.teleopPeriodic();
        this.elevator.teleopPeriodic();
        this.shooter.teleopPeriodic();

        console_debug("End of Teleop");
    }

}