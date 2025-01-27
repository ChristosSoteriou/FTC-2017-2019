package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

public class PictographIdentification {
    private static final String licenseKey = "AdyvN83/////AAAAGUhpzWJYk0bOp0oyBOIZpW4hquQcAMJ7FMwiofLKC5O+sePvN7VpHUTLQ/tI9WwwnSUgYvIX33xHkWJUTXrGQniJtGwReYwyPTUVnKTPWQSF/QVJZgbx0XfetrX3No8/k2BC7bi29jeQyjqfMT39FtgiHvwUoviMdy9xMQC4sw1Nt7rgB4dRa0OTylzGWbrKWBQlsklPoT6ItqHTj03uoqIzjLX3bcn8dMd0erPvmNd/Ub3lYPYorfu5TA1cTVOtsnLrWQVpKl4p9z9+0yrmd+dW4tmVPJB+it/dkmOB+5mfymKPuZZsmtudMLCdn7Iw8lB1UiPoGubcoFAnnL+6cdAwH66EkDU/8EaHt/7Jn0sC";

    HardwareMap hardwareMap = null;

    private VuforiaLocalizer vuforia;
    private RelicRecoveryVuMark cubePosition = RelicRecoveryVuMark.UNKNOWN;

    VuforiaTrackables pictographTrackables;
    VuforiaTrackable pictographTemplate;

    public PictographIdentification() {}


    public void init(HardwareMap hm) {
        hardwareMap = hm;

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters params = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        params.vuforiaLicenseKey = licenseKey;
        params.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        params.cameraMonitorFeedback = VuforiaLocalizer.Parameters.CameraMonitorFeedback.AXES;
        params.useExtendedTracking = false;

        this.vuforia = ClassFactory.createVuforiaLocalizer(params);

        pictographTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        pictographTemplate = pictographTrackables.get(0);
    }
	
	public void start() {
		pictographTrackables.activate();
	}

    public void checkForPictograph() {
        RelicRecoveryVuMark temp = RelicRecoveryVuMark.from(pictographTemplate);
        if (temp != RelicRecoveryVuMark.UNKNOWN) setCubePosition(temp);
    }

    public Orientation getPictographAngle() {
        VuforiaTrackableDefaultListener pictograph = (VuforiaTrackableDefaultListener) pictographTrackables.get(0).getListener();
        OpenGLMatrix pose = pictograph.getPose();

        if (pose == null) return null;

        return Orientation.getOrientation(pose, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
    }

    public RelicRecoveryVuMark getCubePosition() {return cubePosition;}

    public void setCubePosition(RelicRecoveryVuMark position) {cubePosition = position;}
}
