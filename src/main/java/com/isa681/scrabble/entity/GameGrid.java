package com.isa681.scrabble.entity;

import com.isa681.scrabble.controller.ValidationController;
import lombok.Getter;
import lombok.Setter;

public class GameGrid {
    @Getter
    private Character g1;
    @Getter
    private Character g2;
    @Getter
    private Character g3;
    @Getter
    private Character g4;
    @Getter
    private Character g5;
    @Getter
    private Character g6;
    @Getter
    private Character g7;
    @Getter
    private Character g8;
    @Getter
    private Character g9;
    @Getter
    private Character g10;
    @Getter
    private Character g11;
    @Getter
    private Character g12;
    @Getter
    private Character g13;
    @Getter
    private Character g14;
    @Getter
    private Character g15;
    @Getter
    private Character g16;
    @Getter
    private Character g17;
    @Getter
    private Character g18;
    @Getter
    private Character g19;
    @Getter
    private Character g20;
    @Getter
    private Character g21;
    @Getter
    private Character g22;
    @Getter
    private Character g23;
    @Getter
    private Character g24;
    @Getter
    private Character g25;
    @Getter
    private Character g26;
    @Getter
    private Character g27;
    @Getter
    private Character g28;
    @Getter
    private Character g29;
    @Getter
    private Character g30;
    @Getter
    private Character g31;
    @Getter
    private Character g32;
    @Getter
    private Character g33;
    @Getter
    private Character g34;
    @Getter
    private Character g35;
    @Getter
    private Character g36;


    @Getter 
    private Boolean isg1Disabled;
    @Getter 
    private Boolean isg2Disabled;
    @Getter 
    private Boolean isg3Disabled;
    @Getter 
    private Boolean isg4Disabled;
    @Getter 
    private Boolean isg5Disabled;
    @Getter 
    private Boolean isg6Disabled;
    @Getter 
    private Boolean isg7Disabled;
    @Getter 
    private Boolean isg8Disabled;
    @Getter 
    private Boolean isg9Disabled;
    @Getter 
    private Boolean isg10Disabled;
    @Getter 
    private Boolean isg11Disabled;
    @Getter 
    private Boolean isg12Disabled;
    @Getter 
    private Boolean isg13Disabled;
    @Getter 
    private Boolean isg14Disabled;
    @Getter 
    private Boolean isg15Disabled;
    @Getter 
    private Boolean isg16Disabled;
    @Getter 
    private Boolean isg17Disabled;
    @Getter 
    private Boolean isg18Disabled;
    @Getter 
    private Boolean isg19Disabled;
    @Getter 
    private Boolean isg20Disabled;
    @Getter 
    private Boolean isg21Disabled;
    @Getter 
    private Boolean isg22Disabled;
    @Getter 
    private Boolean isg23Disabled;
    @Getter 
    private Boolean isg24Disabled;
    @Getter 
    private Boolean isg25Disabled;
    @Getter 
    private Boolean isg26Disabled;
    @Getter 
    private Boolean isg27Disabled;
    @Getter 
    private Boolean isg28Disabled;
    @Getter 
    private Boolean isg29Disabled;
    @Getter 
    private Boolean isg30Disabled;
    @Getter 
    private Boolean isg31Disabled;
    @Getter 
    private Boolean isg32Disabled;
    @Getter 
    private Boolean isg33Disabled;
    @Getter 
    private Boolean isg34Disabled;
    @Getter 
    private Boolean isg35Disabled;
    @Getter 
    private Boolean isg36Disabled;


    public void setG1(Character g1) {
        ValidationController.validateGameGrid(g1);
        this.g1 = g1;
        if (g1!=null){this.isg1Disabled = true;}
        else {this.isg1Disabled =  false;}
            
    }

    public void setG2(Character g2) {
        ValidationController.validateGameGrid(g2);
        this.g2 = g2;
        if (g2!=null){this.isg2Disabled = true;}
        else {this.isg2Disabled =  false;}
    }

    public void setG3(Character g3) {
        ValidationController.validateGameGrid(g3);
        this.g3 = g3;
        if (g3!=null){this.isg3Disabled = true;}
        else {this.isg3Disabled =  false;}
    }

    public void setG4(Character g4) {
        ValidationController.validateGameGrid(g4);
        this.g4 = g4;
        if (g4!=null){this.isg4Disabled = true;}
        else {this.isg4Disabled =  false;}
    }

    public void setG5(Character g5) {
        ValidationController.validateGameGrid(g5);
        this.g5 = g5;
        if (g5!=null){this.isg5Disabled = true;}
        else {this.isg5Disabled =  false;}
    }

    public void setG6(Character g6) {
        ValidationController.validateGameGrid(g6);
        this.g6 = g6;
        if (g6!=null){this.isg6Disabled = true;}
        else {this.isg6Disabled =  false;}
    }

    public void setG7(Character g7) {
        ValidationController.validateGameGrid(g7);
        this.g7 = g7;
        if (g7!=null){this.isg7Disabled = true;}
        else {this.isg7Disabled =  false;}
    }

    public void setG8(Character g8) {
        ValidationController.validateGameGrid(g8);
        this.g8 = g8;
        if (g8!=null){this.isg8Disabled = true;}
        else {this.isg8Disabled =  false;}
    }

    public void setG9(Character g9) {
        ValidationController.validateGameGrid(g9);
        this.g9 = g9;
        if (g9!=null){this.isg9Disabled = true;}
        else {this.isg9Disabled =  false;}
    }

    public void setG10(Character g10) {
        ValidationController.validateGameGrid(g10);
        this.g10 = g10;
        if (g10!=null){this.isg10Disabled = true;}
        else {this.isg10Disabled =  false;}
    }

    public void setG11(Character g11) {
        ValidationController.validateGameGrid(g11);
        this.g11 = g11;
        if (g11!=null){this.isg11Disabled = true;}
        else {this.isg11Disabled =  false;}
    }

    public void setG12(Character g12) {
        ValidationController.validateGameGrid(g12);
        this.g12 = g12;
        if (g12!=null){this.isg12Disabled = true;}
        else {this.isg12Disabled =  false;}
    }

    public void setG13(Character g13) {
        ValidationController.validateGameGrid(g13);
        this.g13 = g13;
        if (g13!=null){this.isg13Disabled = true;}
        else {this.isg13Disabled =  false;}
    }

    public void setG14(Character g14) {
        ValidationController.validateGameGrid(g14);
        this.g14 = g14;
        if (g14!=null){this.isg14Disabled = true;}
        else {this.isg14Disabled =  false;}
    }

    public void setG15(Character g15) {
        ValidationController.validateGameGrid(g15);
        this.g15 = g15;
        if (g15!=null){this.isg15Disabled = true;}
        else {this.isg15Disabled =  false;}
    }

    public void setG16(Character g16) {
        ValidationController.validateGameGrid(g16);
        this.g16 = g16;
        if (g16!=null){this.isg16Disabled = true;}
        else {this.isg16Disabled =  false;}
    }

    public void setG17(Character g17) {
        ValidationController.validateGameGrid(g17);
        this.g17 = g17;
        if (g17!=null){this.isg17Disabled = true;}
        else {this.isg17Disabled =  false;}
    }

    public void setG18(Character g18) {
        ValidationController.validateGameGrid(g18);
        this.g18 = g18;
        if (g18!=null){this.isg18Disabled = true;}
        else {this.isg18Disabled =  false;}
    }

    public void setG19(Character g19) {
        ValidationController.validateGameGrid(g19);
        this.g19 = g19;
        if (g19!=null){this.isg19Disabled = true;}
        else {this.isg19Disabled =  false;}
    }

    public void setG20(Character g20) {
        ValidationController.validateGameGrid(g20);
        this.g20 = g20;
        if (g20!=null){this.isg20Disabled = true;}
        else {this.isg20Disabled =  false;}
    }

    public void setG21(Character g21) {
        ValidationController.validateGameGrid(g21);
        this.g21 = g21;
        if (g21!=null){this.isg21Disabled = true;}
        else {this.isg21Disabled =  false;}
    }

    public void setG22(Character g22) {
        ValidationController.validateGameGrid(g22);
        this.g22 = g22;
        if (g22!=null){this.isg22Disabled = true;}
        else {this.isg22Disabled =  false;}
    }

    public void setG23(Character g23) {
        ValidationController.validateGameGrid(g23);
        this.g23 = g23;
        if (g23!=null){this.isg23Disabled = true;}
        else {this.isg23Disabled =  false;}
    }

    public void setG24(Character g24) {
        ValidationController.validateGameGrid(g24);
        this.g24 = g24;
        if (g24!=null){this.isg24Disabled = true;}
        else {this.isg24Disabled =  false;}
    }

    public void setG25(Character g25) {
        ValidationController.validateGameGrid(g25);
        this.g25 = g25;
        if (g25!=null){this.isg25Disabled = true;}
        else {this.isg25Disabled =  false;}
    }

    public void setG26(Character g26) {
        ValidationController.validateGameGrid(g26);
        this.g26 = g26;
        if (g26!=null){this.isg26Disabled = true;}
        else {this.isg26Disabled =  false;}
    }

    public void setG27(Character g27) {
        ValidationController.validateGameGrid(g27);
        this.g27 = g27;
        if (g27!=null){this.isg27Disabled = true;}
        else {this.isg27Disabled =  false;}
    }

    public void setG28(Character g28) {
        ValidationController.validateGameGrid(g28);
        this.g28 = g28;
        if (g28!=null){this.isg28Disabled = true;}
        else {this.isg28Disabled =  false;}
    }

    public void setG29(Character g29) {
        ValidationController.validateGameGrid(g29);
        this.g29 = g29;
        if (g29!=null){this.isg29Disabled = true;}
        else {this.isg29Disabled =  false;}
    }

    public void setG30(Character g30) {
        ValidationController.validateGameGrid(g30);
        this.g30 = g30;
        if (g30!=null){this.isg30Disabled = true;}
        else {this.isg30Disabled =  false;}
    }

    public void setG31(Character g31) {
        ValidationController.validateGameGrid(g31);
        this.g31 = g31;
        if (g31!=null){this.isg31Disabled = true;}
        else {this.isg31Disabled =  false;}
    }

    public void setG32(Character g32) {
        ValidationController.validateGameGrid(g32);
        this.g32 = g32;
        if (g32!=null){this.isg32Disabled = true;}
        else {this.isg32Disabled =  false;}
    }

    public void setG33(Character g33) {
        ValidationController.validateGameGrid(g33);
        this.g33 = g33;
        if (g33!=null){this.isg33Disabled = true;}
        else {this.isg33Disabled =  false;}
    }

    public void setG34(Character g34) {
        ValidationController.validateGameGrid(g34);
        this.g34 = g34;
        if (g34!=null){this.isg34Disabled = true;}
        else {this.isg34Disabled =  false;}
    }

    public void setG35(Character g35) {
        ValidationController.validateGameGrid(g35);
        this.g35 = g35;
        if (g35!=null){this.isg35Disabled = true;}
        else {this.isg35Disabled =  false;}
    }

    public void setG36(Character g36) {
        ValidationController.validateGameGrid(g36);
        this.g36 = g36;
        if (g36!=null){this.isg36Disabled = true;}
        else {this.isg36Disabled =  false;}
    }
}
