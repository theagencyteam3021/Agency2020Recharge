package frc.robot;

import frc.robot.AgencySystem;

public class BallHandler extends AgencySystem {

    private Intake intake;
    private Carousel carousel;
    private Elevator elevator;
    private Shooter shooter;

    // private enum ballHandlerMode {
    //     LOADING, SHOOTING
    // };

    private boolean intakeRequested = false;
    private boolean justLoaded = false;
 

    private boolean shotRequested = false;
    private boolean shotInProgress = false;



    private Boolean bailIntakeStage1Request = false;
    private Boolean bailIntakeStage2Request = false;
    private Boolean bailCarouselRequest = false;



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
       // this.lastMode = ballHandlerMode.LOADING;
        this.shotRequested = false;
        this.intakeRequested = true;
    }

    public void stopLoad() {
        this.intakeRequested = false;
    }

    public void shoot() {
        //this.lastMode = ballHandlerMode.SHOOTING;
        this.shotRequested = true;
        this.intakeRequested = false;

    }

    public void changeShootAngle(Shooter.ShooterAngle angle) {
        shooter.requestAngleChange(angle);
    }


    public void bailIntakeStage1(){
        bailIntakeStage1Request = true;
    }
    public void bailIntakeStage2(){
        bailIntakeStage2Request = true;
    }
    public void bailCarousel(){
        bailCarouselRequest = true;
    }

    public void teleopInit() {
        intakeRequested = false;
        shotRequested = false;
       // lastMode = ballHandlerMode.LOADING;
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

        if (intakeRequested){
            intake.requestIntake();
        }
        else {
            intake.requestStop();
        }

        if (elevator.isDown()) { //elevator.isDown()
            shooter.requestStop();
            shotInProgress = false;
        }

        if (!shotInProgress && shotRequested && elevator.hasBall()) {
            shooter.requestStart();
            elevator.requestElevate();
            shotRequested = false;
            shotInProgress = true;
            return;
        }

        if (carousel.isRotating() ) { //&& intake.hasBall()
            console_debug("Carousel Is Rotating RETURNING");
           // intake.requestStop();
            return;
        }

         //SECONDARY BAIL
         if (this.bailIntakeStage1Request){
            intake.reverseIntake();
            return;
        }
        if (this.bailIntakeStage2Request){
            intake.reverseAdvance();
            return;
        }
        if (this.bailCarouselRequest){
            //TODO: 
            ///Reverse one carousel position in the opposite direction that it was travelling
            //carousel.bail();
            //return;
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
      
    }

    public void teleopPeriodic() {
        teleopPeriodicLogic();


        this.intake.teleopPeriodic();
        this.carousel.teleopPeriodic();
        this.elevator.teleopPeriodic();
        this.shooter.teleopPeriodic();
    }

}