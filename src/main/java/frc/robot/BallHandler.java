package frc.robot;

import frc.robot.AgencySystem;

public class BallHandler extends AgencySystem{

    private Intake intake;
    private Carousel carousel;
    private Elevator elevator;
    private Shooter shooter;
    private enum ballHandlerMode{
        LOADING,
        SHOOTING
    };
    private ballHandlerMode lastMode;
    private boolean justLoaded = false;
    private boolean intakeRequested = false;
    private boolean shotRequested = false; 

    public BallHandler(String name, Boolean debug){

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
    public void stopLoad(){
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

    public void teleopInit(){
        lastMode = ballHandlerMode.LOADING;
        carousel.teleopInit();
        elevator.teleopInit();
        
    }

    public void teleopPeriodic() {
        console_debug("teleop");
        // if(elevator.isDown() && !elevator.hasBall()){
        //     shooter.requestStop();
        //     //if(carousel.hasBall3()){
        //      //   elevator.requestOuttake();
        //     //}
        // }
        if(justLoaded && !carousel.isRotating() && !intake.inProgress()){
            carousel.requestForwardAdvance();
            return;
        }
        //TODO: optimize to allow elevator to load at the same time
        else if (intake.inProgress()){
            //TODO: THIS WILL PREVENT TELOP FROM RUNNING!!
           // return;
        }
        else {
            justLoaded = false;
        }
        if(lastMode == ballHandlerMode.LOADING){
            if(elevator.isDown() && !elevator.hasBall() && carousel.hasBall3()){
                console_debug("Loading 1");
                elevator.requestOuttake();
            }
            else if(elevator.hasBall() && intake.hasBall() && !carousel.hasBall2()){
                console_debug("Loading 2");
                carousel.requestForwardAdvance();           
            }
            else if(elevator.hasBall() && intake.hasBall() && !carousel.hasBall3()){
                console_debug("Loading 3");
                carousel.requestReverseAdvance();
            }
            else if((carousel.hasBall1() || carousel.hasBall2()) && !carousel.hasBall3()){
                console_debug("Loading 4");
                carousel.requestReverseAdvance();
            }
            else if(intake.hasBall() && !carousel.hasBall1()){
                console_debug("Loading 5");
                intake.requestAdvance();
                justLoaded = true;
            }
        }
        else if(lastMode == ballHandlerMode.SHOOTING){
            
            if(shotRequested && elevator.hasBall()){
                shooter.requestStart();
                elevator.requestElevate();
                shotRequested = false; 
            }
            if(elevator.isDown() && !elevator.hasBall() && carousel.hasBall3()){
                elevator.requestOuttake();
            }
          /*  else if(elevator.hasBall() && intake.hasBall() && !carousel.hasBall1()){
                intake.requestAdvance();
            }*/
            else if(elevator.hasBall() && intake.hasBall() && !carousel.hasBall2()){
                carousel.requestForwardAdvance();           
            }
            else if(elevator.hasBall() && intake.hasBall() && !carousel.hasBall3()){
                carousel.requestReverseAdvance();
            }
            else if((carousel.hasBall1() || carousel.hasBall2()) && !carousel.hasBall3()){
                carousel.requestReverseAdvance();
            }
            else if(intake.hasBall() && !carousel.hasBall1()){
                intake.requestAdvance();
                justLoaded = true;
            }
        }
        this.intake.teleopPeriodic();
        this.carousel.teleopPeriodic();
        this.elevator.teleopPeriodic();
        this.shooter.teleopPeriodic();
    }

}