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
        lastMode = ballHandlerMode.LOADING;
        intakeRequested = true; 
        shotRequested = false;
    }
    public void stopLoad(){
        intakeRequested = false; 
    }

    public void shoot() {
        lastMode = ballHandlerMode.SHOOTING;
        shotRequested = true;
        intakeRequested = false; 
    }

    public void changeShootAngle(Shooter.ShooterAngle angle) {
        shooter.requestAngleChange(angle);
    }

    public void teleopInit(){
        lastMode = ballHandlerMode.LOADING;
        elevator.teleopInit();
        
    }

    public void teleopPeriodic() {
        
        if(elevator.isDown() && !elevator.hasBall()){
            shooter.requestStop();
            //if(carousel.hasBall3()){
             //   elevator.requestOuttake();
            //}
        }
        if(justLoaded && !carousel.isRotating() && !intake.inProgress()){
            carousel.requestForwardAdvance();
            return;
        }
        //TODO: optimize to allow elevator to load at the same time
        else if (intake.inProgress()){
            return;
        }
        else {
            justLoaded = false;
        }
        if(lastMode == ballHandlerMode.LOADING){
            if(elevator.isDown() && !elevator.hasBall() && carousel.hasBall3()){
                elevator.requestOuttake();
            }
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
    }

}