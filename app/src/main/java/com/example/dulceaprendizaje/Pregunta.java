package com.example.dulceaprendizaje;

public class Pregunta {
    String pregunta;
    String ans1;
    String ans2;
    String ans3;
    String ansCorrecta;
    public Pregunta(String pregunta, String ans1, String ans2, String ans3, String ansCorrecta){
        this.pregunta = pregunta;
        this.ans1 = ans1;
        this.ans2 = ans2;
        this.ans3 = ans3;
        this.ansCorrecta = ansCorrecta;
    }
    public String getPregunta(){return pregunta;}
    public String getAns1() {
        return ans1;
    }

    public String getAns2() {
        return ans2;
    }

    public String getAns3() {
        return ans3;
    }

    public String getAnsCorrecta() {
        return ansCorrecta;
    }

}
