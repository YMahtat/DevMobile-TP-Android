package ma.emsi.tpAndroid;

import android.graphics.Bitmap;

class Etudiant {
    private String nom, prenom, classe, tel;
    private Bitmap photo;
    private int stdID;

    Etudiant(int stdID, String nom, String prenom, Bitmap photo, String classe, String tel) {
        this.stdID = stdID;
        this.nom = nom;
        this.prenom = prenom;
        this.photo = photo;
        this.classe = classe;
        this.tel = tel;
    }


    public void setStdID(int stdID) {
        this.stdID = stdID;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public int getStdID() {
        return stdID;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }
}
