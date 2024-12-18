package com.example.javaapk;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.javaapk.activities.events.EventsActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton PrepareAddButton;
    public Button collector_button;
    public Button button;
    private TextView textview;
    private LinearLayout topLinerLayout;
    private static MainActivity instance;

    public ArrayList<Button> buttonsArrayList = new ArrayList<Button>();
    public int n = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //getSupportActionBar().hide();

        setContentView(R.layout.activity_collector2);

        //Drawable drawable = getResources().getDrawable(R.drawable.menu_layers);
        //getSupportActionBar().setBackgroundDrawable(drawable);


        instance = this;


        for (final Button button : buttonsArrayList) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Open_activity_button_info();
                    System.out.println("anoanoneNENE");
                }
            });
        }
        //buttonInfoOpener(button, buttonsArrayList);


        PrepareAddButton = (FloatingActionButton) findViewById(R.id.createFieldButton);
        textview = (TextView) findViewById(R.id.NeprecteneZpravy);

        topLinerLayout = (LinearLayout) findViewById(R.id.linerLayout);
        PrepareAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Open_activity_new_event();
                //Open_activity_button_info();

                //Intent intent = new Intent(MainActivity.this, button_info.class);
                //startActivity(intent);

            }
        });


        /*TimetableWeek timeTable = null;
        try {
            timeTable = TimetableWeek.parseFromJsonString("{\"lessonTimeIntervals\":[\"07:20 - 08:05\",\"08:10 - 08:55\",\"09:05 - 09:50\",\"10:10 - 10:55\",\"11:05 - 11:50\",\"12:00 - 12:45\",\"12:50 - 13:35\",\"13:40 - 14:25\",\"14:30 - 15:15\",\"15:20 - 16:05\",\"16:10 - 16:55\"],\"days\":[{\"date\":\"20.11.\",\"dayOfWeek\":\"Po\",\"lessonRows\":[{\"lessons\":[{\"classroomShortcut\":\"U7A\",\"assessments\":[],\"subjectFullName\":\"ČjL (Jazyk český) \",\"subjectShortcut\":\"ČjL\",\"groupShortcut\":\"7.A\",\"type\":\"REGULAR\",\"furtherInfo\":[{\"name\":\"Učitel\",\"value\":\"Piňosová R.\"},{\"name\":\"Třída\",\"value\":\"7.A\"},{\"name\":\"Žáci\",\"value\":\"7.A (celá třída)\"},{\"name\":\"Učebna\",\"value\":\"U7A\"},{\"name\":\"Cyklus\",\"value\":\"bez cyklů\"},{\"name\":\"Den (vyuč. hodina)\",\"value\":\"Po 20.11. (0)\"},{\"name\":\"Probrané učivo\",\"value\":\"Literární moderna\"}]},{\"classroomShortcut\":\"U7A\",\"assessments\":[{\"subject\":\"M (Matematika) \",\"showPopupWindowURL\":\"..\\/Hodnoceni\\/KHO010_HodnVypisDetail.aspx?UdalostID=C40484579&OsobaID=C3385762\",\"furtherInfo\":[{\"name\":\"Druh hodnocení:\",\"value\":\"Hodnocení váhy 10% [váha 0,10]\"},{\"name\":\"Téma:\",\"value\":\"Hodnocení samostatné práce\"},{\"name\":\"Slovní hodnocení:\",\"value\":\"neodevzdal\"},{\"name\":\"Den:\",\"value\":\"Po 20.11.\"},{\"name\":\"Hodina:\",\"value\":\"1\"}]}],\"subjectFullName\":\"M (Matematika) \",\"subjectShortcut\":\"M\",\"groupShortcut\":\"7.A\",\"type\":\"REGULAR\",\"furtherInfo\":[{\"name\":\"Učitel\",\"value\":\"Chmela J.\"},{\"name\":\"Třída\",\"value\":\"7.A\"},{\"name\":\"Žáci\",\"value\":\"7.A (celá třída)\"},{\"name\":\"Učebna\",\"value\":\"U7A\"},{\"name\":\"Cyklus\",\"value\":\"bez cyklů\"},{\"name\":\"Den (vyuč. hodina)\",\"value\":\"Po 20.11. (1)\"},{\"name\":\"Probrané učivo\",\"value\":\"Geometrická posloupnost\"}]},{\"classroomShortcut\":\"PFy\",\"assessments\":[],\"subjectFullName\":\"Fy (Fyzika) \",\"subjectShortcut\":\"Fy\",\"groupShortcut\":\"7.A\",\"type\":\"REGULAR\",\"furtherInfo\":[{\"name\":\"Učitel\",\"value\":\"Říman P.\"},{\"name\":\"Třída\",\"value\":\"7.A\"},{\"name\":\"Žáci\",\"value\":\"7.A (celá třída)\"},{\"name\":\"Učebna\",\"value\":\"PFy\"},{\"name\":\"Cyklus\",\"value\":\"bez cyklů\"},{\"name\":\"Den (vyuč. hodina)\",\"value\":\"Po 20.11. (2)\"},{\"name\":\"Probrané učivo\",\"value\":\"El. potenciál, napětí - cvičení.\"}]},{\"classroomShortcut\":\"U7A\",\"assessments\":[],\"subjectFullName\":\"M (Matematika) \",\"subjectShortcut\":\"M\",\"groupShortcut\":\"7.A\",\"type\":\"REPLACEMENT\",\"furtherInfo\":[{\"name\":\"Učitel\",\"value\":\"Chmela J.\"},{\"name\":\"Třída\",\"value\":\"7.A\"},{\"name\":\"Žáci\",\"value\":\"7.A (A1)\"},{\"name\":\"Učebna\",\"value\":\"U7A\"},{\"name\":\"Cyklus\",\"value\":\"bez cyklů\"},{\"name\":\"Den (vyuč. hodina)\",\"value\":\"Po 20.11. (3)\"},{\"name\":\"Nahrazuje hodiny\",\"value\":\"Po 20.11. 3, Aj (Angličtina), Šuhajová M., 7.A (A1), U7B\"},{\"name\":\"Probrané učivo\",\"value\":\"Opakování - geometrická posloupnost\"}]},{\"classroomShortcut\":\"U7A\",\"assessments\":[],\"subjectFullName\":\"Aj (Angličtina) \",\"subjectShortcut\":\"Aj\",\"groupShortcut\":\"7.A\",\"type\":\"REPLACEMENT\",\"furtherInfo\":[{\"name\":\"Učitel\",\"value\":\"Šuhajová M.\"},{\"name\":\"Třída\",\"value\":\"7.A\"},{\"name\":\"Žáci\",\"value\":\"7.A (A1)\"},{\"name\":\"Učebna\",\"value\":\"U7A\"},{\"name\":\"Cyklus\",\"value\":\"bez cyklů\"},{\"name\":\"Den (vyuč. hodina)\",\"value\":\"Po 20.11. (4)\"},{\"name\":\"Nahrazuje hodiny\",\"value\":\"Po 20.11. 4, M (Matematika), Chmela J., 7.A (A1), U7A\"},{\"name\":\"Probrané učivo\",\"value\":\"L 5C - trénink poslechu; konverzace, frázová slovesa\"}]},{\"classroomShortcut\":\"U7A\",\"assessments\":[],\"subjectFullName\":\"Šj (Španělština) \",\"subjectShortcut\":\"Šj\",\"groupShortcut\":\"7.A\",\"type\":\"REPLACEMENT\",\"furtherInfo\":[{\"name\":\"Učitel\",\"value\":\"Šuhajová M.\"},{\"name\":\"Třída\",\"value\":\"7.A\"},{\"name\":\"Žáci\",\"value\":\"7.A (Šj)\"},{\"name\":\"Učebna\",\"value\":\"U7A\"},{\"name\":\"Cyklus\",\"value\":\"bez cyklů\"},{\"name\":\"Den (vyuč. hodina)\",\"value\":\"Po 20.11. (5)\"},{\"name\":\"Nahrazuje hodiny\",\"value\":\"Po 20.11. 5, Šj (Španělština), <span class=AbsZdroj>Číhalová M.<\\/span>, 7.A (Šj), UJ2\"},{\"name\":\"Probrané učivo\",\"value\":\"Imperfektum a příslovce\"}]},{\"isEmpty\":1},{\"isEmpty\":1},{\"isEmpty\":1},{\"isEmpty\":1},{\"isEmpty\":1}]},{\"lessons\":[{\"isEmpty\":1},{\"isEmpty\":1},{\"isEmpty\":1},{\"classroomShortcut\":\"U7B\",\"assessments\":[{\"subject\":\"M (Matematika) \",\"showPopupWindowURL\":\"..\\/Hodnoceni\\/KHO010_HodnVypisDetail.aspx?UdalostID=C40471946&OsobaID=C3385762\",\"furtherInfo\":[{\"name\":\"Druh hodnocení:\",\"value\":\"Hodnocení váhy 10% [váha 0,10]\"},{\"name\":\"Téma:\",\"value\":\"Posloupnost, důkaz monotónnosti posloupnosti\"},{\"name\":\"Výsledek:\",\"value\":\"1\"},{\"name\":\"Den:\",\"value\":\"Po 20.11.\"},{\"name\":\"Hodina:\",\"value\":\"3\"}]}],\"subjectFullName\":\"Aj (Angličtina) \",\"subjectShortcut\":\"Aj\",\"groupShortcut\":\"7.A\",\"type\":\"REPLACED\",\"furtherInfo\":[{\"name\":\"Učitel\",\"value\":\"Šuhajová M.\"},{\"name\":\"Třída\",\"value\":\"7.A\"},{\"name\":\"Žáci\",\"value\":\"7.A (A1)\"},{\"name\":\"Učebna\",\"value\":\"U7B\"},{\"name\":\"Cyklus\",\"value\":\"bez cyklů\"},{\"name\":\"Den (vyuč. hodina)\",\"value\":\"Po 20.11. (3)\"},{\"name\":\"Je suplována hodinami\",\"value\":\"Po 20.11. 3, M (Matematika), Chmela J., 7.A (A1), U7A\"}]},{\"classroomShortcut\":\"U7A\",\"assessments\":[],\"subjectFullName\":\"M (Matematika) \",\"subjectShortcut\":\"M\",\"groupShortcut\":\"7.A\",\"type\":\"REPLACED\",\"furtherInfo\":[{\"name\":\"Učitel\",\"value\":\"Chmela J.\"},{\"name\":\"Třída\",\"value\":\"7.A\"},{\"name\":\"Žáci\",\"value\":\"7.A (A1)\"},{\"name\":\"Učebna\",\"value\":\"U7A\"},{\"name\":\"Cyklus\",\"value\":\"bez cyklů\"},{\"name\":\"Den (vyuč. hodina)\",\"value\":\"Po 20.11. (4)\"},{\"name\":\"Je suplována hodinami\",\"value\":\"Po 20.11. 4, Aj (Angličtina), Šuhajová M., 7.A (A1), U7A\"}]},{\"classroomShortcut\":\"UJ2\",\"assessments\":[],\"subjectFullName\":\"Šj (Španělština) \",\"subjectShortcut\":\"Šj\",\"groupShortcut\":\"7.A\",\"type\":\"REPLACED\",\"furtherInfo\":[{\"name\":\"Učitel\",\"value\":\"<span class=AbsZdroj>Číhalová M.<\\/span>\"},{\"name\":\"Třída\",\"value\":\"7.A\"},{\"name\":\"Žáci\",\"value\":\"7.A (Šj)\"},{\"name\":\"Učebna\",\"value\":\"UJ2\"},{\"name\":\"Cyklus\",\"value\":\"bez cyklů\"},{\"name\":\"Den (vyuč. hodina)\",\"value\":\"Po 20.11. (5)\"},{\"name\":\"Je suplována hodinami\",\"value\":\"Po 20.11. 5, Šj (Španělština), Šuhajová M., 7.A (Šj), U7A\"}]},{\"isEmpty\":1},{\"isEmpty\":1},{\"isEmpty\":1},{\"isEmpty\":1},{\"isEmpty\":1}]},{\"lessons\":[{\"isEmpty\":1},{\"isEmpty\":1},{\"isEmpty\":1},{\"isEmpty\":1},{\"isEmpty\":1},{\"isEmpty\":1},{\"isEmpty\":1},{\"isEmpty\":1},{\"isEmpty\":1},{\"isEmpty\":1},{\"isEmpty\":1}]}]},{\"date\":\"21.11.\",\"dayOfWeek\":\"Út\",\"lessonRows\":[{\"lessons\":[{\"classroomShortcut\":\"7.B\",\"assessments\":[],\"subjectFullName\":\"MCv (Cvičení z matematiky) \",\"subjectShortcut\":\"MCv\",\"groupShortcut\":\"7.A,\",\"type\":\"REGULAR\",\"furtherInfo\":[{\"name\":\"Učitel\",\"value\":\"Kahánková H.\"},{\"name\":\"Třída\",\"value\":\"7.A, 7.B\"},{\"name\":\"Žáci\",\"value\":\"7.A (MCv), 7.B (MCv)\"},{\"name\":\"Učebna\",\"value\":\"U7A\"},{\"name\":\"Cyklus\",\"value\":\"bez cyklů\"},{\"name\":\"Den (vyuč. hodina)\",\"value\":\"Út 21.11. (0)\"},{\"name\":\"Probrané učivo\",\"value\":\"Gaussova rovina, řešení kvadratické rovnice v C\"}]},{\"classroomShortcut\":\"7.B\",\"assessments\":[],\"subjectFullName\":\"MCv (Cvičení z matematiky) \",\"subjectShortcut\":\"MCv\",\"groupShortcut\":\"7.A,\",\"type\":\"REGULAR\",\"furtherInfo\":[{\"name\":\"Učitel\",\"value\":\"Kahánková H.\"},{\"name\":\"Třída\",\"value\":\"7.A, 7.B\"},{\"name\":\"Žáci\",\"value\":\"7.A (MCv), 7.B (MCv)\"},{\"name\":\"Učebna\",\"value\":\"U7A\"},{\"name\":\"Cyklus\",\"value\":\"bez cyklů\"},{\"name\":\"Den (vyuč. hodina)\",\"value\":\"Út 21.11. (0)\"},{\"name\":\"Probrané učivo\",\"value\":\"Gaussova rovina, řešení kvadratické rovnice v C\"}]},{\"classroomShortcut\":\"U7A\",\"assessments\":[],\"subjectFullName\":\"Zsv (Základy spol. věd) \",\"subjectShortcut\":\"Zsv\",\"groupShortcut\":\"7.A\",\"type\":\"REGULAR\",\"furtherInfo\":[{\"name\":\"Učitel\",\"value\":\"Skálová M.\"},{\"name\":\"Třída\",\"value\":\"7.A\"},{\"name\":\"Žáci\",\"value\":\"7.A (celá třída)\"},{\"name\":\"Učebna\",\"value\":\"U7A\"},{\"name\":\"Cyklus\",\"value\":\"bez cyklů\"},{\"name\":\"Den (vyuč. hodina)\",\"value\":\"Út 21.11. (2)\"},{\"name\":\"Probrané učivo\",\"value\":\"Trestní právo hmotné.\"}]},{\"classroomShortcut\":\"U7A\",\"assessments\":[{\"subject\":\"M (Matematika) \",\"showPopupWindowURL\":\"..\\/Hodnoceni\\/KHO010_HodnVypisDetail.aspx?UdalostID=C40490686&OsobaID=C3385762\",\"furtherInfo\":[{\"name\":\"Druh hodnocení:\",\"value\":\"Hodnocení váhy 10% [váha 0,10]\"},{\"name\":\"Téma:\",\"value\":\"aritmetická posloupnost\"},{\"name\":\"Výsledek:\",\"value\":\"1\"},{\"name\":\"Den:\",\"value\":\"Út 21.11.\"},{\"name\":\"Hodina:\",\"value\":\"3\"}]}],\"subjectFullName\":\"M (Matematika) \",\"subjectShortcut\":\"M\",\"groupShortcut\":\"7.A\",\"type\":\"REGULAR\",\"furtherInfo\":[{\"name\":\"Učitel\",\"value\":\"Chmela J.\"},{\"name\":\"Třída\",\"value\":\"7.A\"},{\"name\":\"Žáci\",\"value\":\"7.A (celá třída)\"},{\"name\":\"Učebna\",\"value\":\"U7A\"},{\"name\":\"Cyklus\",\"value\":\"bez cyklů\"},{\"name\":\"Den (vyuč. hodina)\",\"value\":\"Út 21.11. (3)\"},{\"name\":\"Probrané učivo\",\"value\":\"Užití aritmetických a geometrických posloupností\"}]},{\"classroomShortcut\":\"U7A\",\"assessments\":[],\"subjectFullName\":\"Aj (Angličtina) \",\"subjectShortcut\":\"Aj\",\"groupShortcut\":\"7.A\",\"type\":\"REGULAR\",\"furtherInfo\":[{\"name\":\"Učitel\",\"value\":\"Šuhajová M.\"},{\"name\":\"Třída\",\"value\":\"7.A\"},{\"name\":\"Žáci\",\"value\":\"7.A (A1)\"},{\"name\":\"Učebna\",\"value\":\"U7A\"},{\"name\":\"Cyklus\",\"value\":\"bez cyklů\"},{\"name\":\"Den (vyuč. hodina)\",\"value\":\"Út 21.11. (4)\"},{\"name\":\"Probrané učivo\",\"value\":\"U 5C - trénink poslechu; formální a neformální omluvení se\"}]},{\"isEmpty\":1},{\"classroomShortcut\":\"7.B,\",\"assessments\":[],\"subjectFullName\":\"C1A (Seminář ke zkoušce C1 Advanced) \",\"subjectShortcut\":\"C1A\",\"groupShortcut\":\"7.A,\",\"type\":\"REGULAR\",\"furtherInfo\":[{\"name\":\"Učitel\",\"value\":\"Foltýnová Glacová L.\"},{\"name\":\"Třída\",\"value\":\"7.A, 7.B, 8.A, 8.B\"},{\"name\":\"Žáci\",\"value\":\"7.A (C1A), 7.B (C1A), 8.A (C1A), 8.B (C1A)\"},{\"name\":\"Učebna\",\"value\":\"UJ2\"},{\"name\":\"Cyklus\",\"value\":\"bez cyklů\"},{\"name\":\"Den (vyuč. hodina)\",\"value\":\"Út 21.11. (6)\"},{\"name\":\"Probrané učivo\",\"value\":\"Lekce 13, dílčí části zkoušky C1 Advanced, esej\"}]},{\"classroomShortcut\":\"7.B,\",\"assessments\":[],\"subjectFullName\":\"C1A (Seminář ke zkoušce C1 Advanced) \",\"subjectShortcut\":\"C1A\",\"groupShortcut\":\"7.A,\",\"type\":\"REGULAR\",\"furtherInfo\":[{\"name\":\"Učitel\",\"value\":\"Foltýnová Glacová L.\"},{\"name\":\"Třída\",\"value\":\"7.A, 7.B, 8.A, 8.B\"},{\"name\":\"Žáci\",\"value\":\"7.A (C1A), 7.B (C1A), 8.A (C1A), 8.B (C1A)\"},{\"name\":\"Učebna\",\"value\":\"UJ2\"},{\"name\":\"Cyklus\",\"value\":\"bez cyklů\"},{\"name\":\"Den (vyuč. hodina)\",\"value\":\"Út 21.11. (6)\"},{\"name\":\"Probrané učivo\",\"value\":\"Lekce 13, dílčí části zkoušky C1 Advanced, esej\"}]},{\"isEmpty\":1},{\"isEmpty\":1},{\"isEmpty\":1}]},{\"lessons\":[{\"isEmpty\":1},{\"isEmpty\":1},{\"isEmpty\":1},{\"isEmpty\":1},{\"isEmpty\":1},{\"isEmpty\":1},{\"isEmpty\":1},{\"isEmpty\":1},{\"isEmpty\":1},{\"isEmpty\":1},{\"isEmpty\":1}]}]},{\"date\":\"22.11.\",\"dayOfWeek\":\"St\",\"lessonRows\":[{\"lessons\":[{\"isEmpty\":1},{\"isEmpty\":1},{\"classroomShortcut\":\"U7A\",\"assessments\":[],\"subjectFullName\":\"Zsv (Základy spol. věd) \",\"subjectShortcut\":\"Zsv\",\"groupShortcut\":\"7.A\",\"type\":\"REPLACEMENT\",\"furtherInfo\":[{\"name\":\"Učitel\",\"value\":\"Skálová M.\"},{\"name\":\"Třída\",\"value\":\"7.A\"},{\"name\":\"Žáci\",\"value\":\"7.A (celá třída)\"},{\"name\":\"Učebna\",\"value\":\"U7A\"},{\"name\":\"Cyklus\",\"value\":\"bez cyklů\"},{\"name\":\"Den (vyuč. hodina)\",\"value\":\"St 22.11. (2)\"},{\"name\":\"Nahrazuje hodiny\",\"value\":\"St 22.11. 2, D (Dějepis), <span class=AbsZdroj>Skotnicová E.<\\/span>, 7.A (celá třída), U7A\"},{\"name\":\"Probrané učivo\",\"value\":\"Trestní právo hmotné.\"}]},{\"classroomShortcut\":\"U7A\",\"assessments\":[],\"subjectFullName\":\"ČjL (Jazyk český) \",\"subjectShortcut\":\"ČjL\",\"groupShortcut\":\"7.A\",\"type\":\"REGULAR\",\"furtherInfo\":[{\"name\":\"Učitel\",\"value\":\"Piňosová R.\"},{\"name\":\"Třída\",\"value\":\"7.A\"},{\"name\":\"Žáci\",\"value\":\"7.A (celá třída)\"},{\"name\":\"Učebna\",\"value\":\"U7A\"},{\"name\":\"Cyklus\",\"value\":\"bez cyklů\"},{\"name\":\"Den (vyuč. hodina)\",\"value\":\"St 22.11. (3)\"},{\"name\":\"Probrané učivo\",\"value\":\"Česká literární moderna; Manifest České moderny\"}]},{\"classroomShortcut\":\"U7A\",\"assessments\":[],\"subjectFullName\":\"Šj (Španělština) \",\"subjectShortcut\":\"Šj\",\"groupShortcut\":\"7.A\",\"type\":\"REPLACEMENT\",\"furtherInfo\":[{\"name\":\"Učitel\",\"value\":\"Šuhajová M.\"},{\"name\":\"Třída\",\"value\":\"7.A\"},{\"name\":\"Žáci\",\"value\":\"7.A (Šj)\"},{\"name\":\"Učebna\",\"value\":\"U7A\"},{\"name\":\"Cyklus\",\"value\":\"bez cyklů\"},{\"name\":\"Den (vyuč. hodina)\",\"value\":\"St 22.11. (4)\"},{\"name\":\"Nahrazuje hodiny\",\"value\":\"St 22.11. 4, Šj (Španělština), <span class=AbsZdroj>Číhalová M.<\\/span>, 7.A (Šj), U5A\"},{\"name\":\"Probrané učivo\",\"value\":\"Minulý čas prostý, práce s textem\"}]},{\"classroomShortcut\":\"TV1\",\"assessments\":[],\"subjectFullName\":\"Tv\\/c (Tělesná výchova - chlapci) \",\"subjectShortcut\":\"Tv\\/c\",\"groupShortcut\":\"7.A\",\"type\":\"REGULAR\",\"furtherInfo\":[{\"name\":\"Učitel\",\"value\":\"Hýža M.\"},{\"name\":\"Třída\",\"value\":\"7.A\"},{\"name\":\"Žáci\",\"value\":\"7.A (Chlapci)\"},{\"name\":\"Učebna\",\"value\":\"TV1\"},{\"name\":\"Cyklus\",\"value\":\"bez cyklů\"},{\"name\":\"Den (vyuč. hodina)\",\"value\":\"St 22.11. (5)\"},{\"name\":\"Probrané učivo\",\"value\":\"Kondiční příprava (švihadlo), florbal\"}]},{\"isEmpty\":1},{\"classroomShortcut\":\"U7A\",\"assessments\":[],\"subjectFullName\":\"Ch (Chemie) \",\"subjectShortcut\":\"Ch\",\"groupShortcut\":\"7.A\",\"type\":\"REGULAR\",\"furtherInfo\":[{\"name\":\"Učitel\",\"value\":\"Slaničanová H.\"},{\"name\":\"Třída\",\"value\":\"7.A\"},{\"name\":\"Žáci\",\"value\":\"7.A (celá třída)\"},{\"name\":\"Učebna\",\"value\":\"U7A\"},{\"name\":\"Cyklus\",\"value\":\"bez cyklů\"},{\"name\":\"Den (vyuč. hodina)\",\"value\":\"St 22.11. (7)\"},{\"name\":\"Probrané učivo\",\"value\":\"Areny - zástupci\"}]},{\"classroomShortcut\":\"PBi\",\"assessments\":[],\"subjectFullName\":\"Bi (Biologie) \",\"subjectShortcut\":\"Bi\",\"groupShortcut\":\"7.A\",\"type\":\"REGULAR\",\"furtherInfo\":[{\"name\":\"Učitel\",\"value\":\"Dudová M.\"},{\"name\":\"Třída\",\"value\":\"7.A\"},{\"name\":\"Žáci\",\"value\":\"7.A (celá třída)\"},{\"name\":\"Učebna\",\"value\":\"PBi\"},{\"name\":\"Cyklus\",\"value\":\"bez cyklů\"},{\"name\":\"Den (vyuč. hodina)\",\"value\":\"St 22.11. (8)\"},{\"name\":\"Probrané učivo\",\"value\":\"Nemoci svalové soustavy.\"}]},{\"isEmpty\":1},{\"isEmpty\":1}]},{\"lessons\":[{\"isEmpty\":1},{\"isEmpty\":1},{\"classroomShortcut\":\"U7A\",\"assessments\":[{\"subject\":\"Zsv (Základy spol. věd) \",\"showPopupWindowURL\":\"..\\/Hodnoceni\\/KHO010_HodnVypisDetail.aspx?UdalostID=C40508460&OsobaID=C3385762\",\"furtherInfo\":[{\"name\":\"Druh hodnocení:\",\"value\":\"Hodnocení váhy 70% [váha 0,70]\"},{\"name\":\"Téma:\",\"value\":\"Ekonomická olympiáda.\"},{\"name\":\"Výsledek:\",\"value\":\"1\"},{\"name\":\"Den:\",\"value\":\"St 22.11.\"},{\"name\":\"Hodina:\",\"value\":\"2\"}]}],\"subjectFullName\":\"D (Dějepis) \",\"subjectShortcut\":\"D\",\"groupShortcut\":\"7.A\",\"type\":\"REPLACED\",\"furtherInfo\":[{\"name\":\"Učitel\",\"value\":\"<span class=AbsZdroj>Skotnicová E.<\\/span>\"},{\"name\":\"Třída\",\"value\":\"7.A\"},{\"name\":\"Žáci\",\"value\":\"7.A (celá třída)\"},{\"name\":\"Učebna\",\"value\":\"U7A\"},{\"name\":\"Cyklus\",\"value\":\"bez cyklů\"},{\"name\":\"Den (vyuč. hodina)\",\"value\":\"St 22.11. (2)\"},{\"name\":\"Je suplována hodinami\",\"value\":\"St 22.11. 2, Zsv (Základy spol. věd), Skálová M., 7.A (celá třída), U7A\"}]},{\"isEmpty\":1},{\"classroomShortcut\":\"U5A\",\"assessments\":[],\"subjectFullName\":\"Šj (Španělština) \",\"subjectShortcut\":\"Šj\",\"groupShortcut\":\"7.A\",\"type\":\"REPLACED\",\"furtherInfo\":[{\"name\":\"Učitel\",\"value\":\"<span class=AbsZdroj>Číhalová M.<\\/span>\"},{\"name\":\"Třída\",\"value\":\"7.A\"},{\"name\":\"Žáci\",\"value\":\"7.A (Šj)\"},{\"name\":\"Učebna\",\"value\":\"U5A\"},{\"name\":\"Cyklus\",\"value\":\"bez cyklů\"},{\"name\":\"Den (vyuč. hodina)\",\"value\":\"St 22.11. (4)\"},{\"name\":\"Je suplována hodinami\",\"value\":\"St 22.11. 4, Šj (Španělština), Šuhajová M., 7.A (Šj), U7A\"}]},{\"isEmpty\":1},{\"isEmpty\":1},{\"isEmpty\":1},{\"isEmpty\":1},{\"isEmpty\":1},{\"isEmpty\":1}]},{\"lessons\":[{\"isEmpty\":1},{\"isEmpty\":1},{\"isEmpty\":1},{\"isEmpty\":1},{\"isEmpty\":1},{\"isEmpty\":1},{\"isEmpty\":1},{\"isEmpty\":1},{\"isEmpty\":1},{\"isEmpty\":1},{\"isEmpty\":1}]}]},{\"date\":\"23.11.\",\"dayOfWeek\":\"Čt\",\"lessonRows\":[{\"lessons\":[{\"classroomShortcut\":\"VT3\",\"assessments\":[],\"subjectFullName\":\"D (Dějepis) \",\"subjectShortcut\":\"D\",\"groupShortcut\":\"7.A\",\"type\":\"REPLACED\",\"furtherInfo\":[{\"name\":\"Učitel\",\"value\":\"<span class=AbsZdroj>Skotnicová E.<\\/span>\"},{\"name\":\"Třída\",\"value\":\"7.A\"},{\"name\":\"Žáci\",\"value\":\"7.A (celá třída)\"},{\"name\":\"Učebna\",\"value\":\"VT3\"},{\"name\":\"Cyklus\",\"value\":\"bez cyklů\"},{\"name\":\"Den (vyuč. hodina)\",\"value\":\"Čt 23.11. (0)\"},{\"name\":\"Je suplována hodinami\",\"value\":\"Hodina odpadá\"}]},{\"classroomShortcut\":\"VT3\",\"assessments\":[],\"subjectFullName\":\"Ekonomická olympiáda \",\"subjectShortcut\":\"Ekonomická olympiáda\",\"groupShortcut\":\"7.A\",\"type\":\"EVENT\",\"furtherInfo\":[{\"name\":\"Učitel\",\"value\":\"Skálová M.\"},{\"name\":\"Třída\",\"value\":\"7.A\"},{\"name\":\"Žáci\",\"value\":\"7.A (celá třída)\"},{\"name\":\"Učebna\",\"value\":\"VT3\"},{\"name\":\"Cyklus\",\"value\":\"bez cyklů\"},{\"name\":\"Den (vyuč. hodina)\",\"value\":\"Čt 23.11. (1)\"},{\"name\":\"Probrané učivo\",\"value\":\"Ekonomická olympiáda.\"}]},{\"classroomShortcut\":\"U7A\",\"assessments\":[],\"subjectFullName\":\"M (Matematika) \",\"subjectShortcut\":\"M\",\"groupShortcut\":\"7.A\",\"type\":\"REGULAR\",\"furtherInfo\":[{\"name\":\"Učitel\",\"value\":\"Chmela J.\"},{\"name\":\"Třída\",\"value\":\"7.A\"},{\"name\":\"Žáci\",\"value\":\"7.A (celá třída)\"},{\"name\":\"Učebna\",\"value\":\"U7A\"},{\"name\":\"Cyklus\",\"value\":\"bez cyklů\"},{\"name\":\"Den (vyuč. hodina)\",\"value\":\"Čt 23.11. (2)\"},{\"name\":\"Probrané učivo\",\"value\":\"Slovní úlohy - aritmetická a geometrická posloupnost\"}]},{\"classroomShortcut\":\"PFy\",\"assessments\":[],\"subjectFullName\":\"Fy (Fyzika) \",\"subjectShortcut\":\"Fy\",\"groupShortcut\":\"7.A\",\"type\":\"REPLACEMENT\",\"furtherInfo\":[{\"name\":\"Učitel\",\"value\":\"Říman P.\"},{\"name\":\"Třída\",\"value\":\"7.A\"},{\"name\":\"Žáci\",\"value\":\"7.A (celá třída)\"},{\"name\":\"Učebna\",\"value\":\"PFy\"},{\"name\":\"Cyklus\",\"value\":\"bez cyklů\"},{\"name\":\"Den (vyuč. hodina)\",\"value\":\"Čt 23.11. (3)\"},{\"name\":\"Nahrazuje hodiny\",\"value\":\"Čt 23.11. 3, Fy (Fyzika), Říman P., 7.A (celá třída), U7A\"},{\"name\":\"Probrané učivo\",\"value\":\"Elektrické pole - cvičení.\"}]},{\"classroomShortcut\":\"U7A\",\"assessments\":[{\"subject\":\"Ch (Chemie) \",\"showPopupWindowURL\":\"..\\/Hodnoceni\\/KHO010_HodnVypisDetail.aspx?UdalostID=C40513695&OsobaID=C3385762\",\"furtherInfo\":[{\"name\":\"Druh hodnocení:\",\"value\":\"Hodnocení váhy 60% [váha 0,60]\"},{\"name\":\"Téma:\",\"value\":\"Zkoušení\"},{\"name\":\"Výsledek:\",\"value\":\"4\"},{\"name\":\"Den:\",\"value\":\"Čt 23.11.\"},{\"name\":\"Hodina:\",\"value\":\"4\"}]}],\"subjectFullName\":\"Ch (Chemie) \",\"subjectShortcut\":\"Ch\",\"groupShortcut\":\"7.A\",\"type\":\"REGULAR\",\"furtherInfo\":[{\"name\":\"Učitel\",\"value\":\"Slaničanová H.\"},{\"name\":\"Třída\",\"value\":\"7.A\"},{\"name\":\"Žáci\",\"value\":\"7.A (celá třída)\"},{\"name\":\"Učebna\",\"value\":\"U7A\"},{\"name\":\"Cyklus\",\"value\":\"bez cyklů\"},{\"name\":\"Den (vyuč. hodina)\",\"value\":\"Čt 23.11. (4)\"},{\"name\":\"Probrané učivo\",\"value\":\"Deriváty uhlovodíků\"}]},{\"classroomShortcut\":\"U7A\",\"assessments\":[],\"subjectFullName\":\"ČjL (Jazyk český) \",\"subjectShortcut\":\"ČjL\",\"groupShortcut\":\"7.A\",\"type\":\"REGULAR\",\"furtherInfo\":[{\"name\":\"Učitel\",\"value\":\"Piňosová R.\"},{\"name\":\"Třída\",\"value\":\"7.A\"},{\"name\":\"Žáci\",\"value\":\"7.A (celá třída)\"},{\"name\":\"Učebna\",\"value\":\"U7A\"},{\"name\":\"Cyklus\",\"value\":\"bez cyklů\"},{\"name\":\"Den (vyuč. hodina)\",\"value\":\"Čt 23.11. (5)\"},{\"name\":\"Probrané učivo\",\"value\":\"Manifest České moderny; F. X. Šalda\"}]},{\"isEmpty\":1},{\"isEmpty\":1},{\"isEmpty\":1},{\"isEmpty\":1},{\"isEmpty\":1}]},{\"lessons\":[{\"isEmpty\":1},{\"classroomShortcut\":\"U7A\",\"assessments\":[],\"subjectFullName\":\"Zsv (Základy spol. věd) \",\"subjectShortcut\":\"Zsv\",\"groupShortcut\":\"7.A\",\"type\":\"REPLACED\",\"furtherInfo\":[{\"name\":\"Učitel\",\"value\":\"<span class=AbsZdroj>Skálová M.<\\/span>\"},{\"name\":\"Třída\",\"value\":\"<span class=AbsZdroj>7.A<\\/span>\"},{\"name\":\"Žáci\",\"value\":\"<span class=AbsZdroj>7.A (celá třída)<\\/span>\"},{\"name\":\"Učebna\",\"value\":\"U7A\"},{\"name\":\"Cyklus\",\"value\":\"bez cyklů\"},{\"name\":\"Den (vyuč. hodina)\",\"value\":\"Čt 23.11. (1)\"},{\"name\":\"Je suplována hodinami\",\"value\":\"Hodina odpadá\"}]},{\"isEmpty\":1},{\"classroomShortcut\":\"U7A\",\"assessments\":[],\"subjectFullName\":\"Fy (Fyzika) \",\"subjectShortcut\":\"Fy\",\"groupShortcut\":\"7.A\",\"type\":\"REPLACED\",\"furtherInfo\":[{\"name\":\"Učitel\",\"value\":\"Říman P.\"},{\"name\":\"Třída\",\"value\":\"7.A\"},{\"name\":\"Žáci\",\"value\":\"7.A (celá třída)\"},{\"name\":\"Učebna\",\"value\":\"U7A\"},{\"name\":\"Cyklus\",\"value\":\"bez cyklů\"},{\"name\":\"Den (vyuč. hodina)\",\"value\":\"Čt 23.11. (3)\"},{\"name\":\"Je suplována hodinami\",\"value\":\"Čt 23.11. 3, Fy (Fyzika), Říman P., 7.A (celá třída), PFy\"},{\"name\":\"Probrané učivo\",\"value\":\"Elektrické pole - cvičení.\"}]},{\"isEmpty\":1},{\"isEmpty\":1},{\"isEmpty\":1},{\"isEmpty\":1},{\"isEmpty\":1},{\"isEmpty\":1},{\"isEmpty\":1}]}]},{\"date\":\"24.11.\",\"dayOfWeek\":\"Pá\",\"lessonRows\":[{\"lessons\":[{\"classroomShortcut\":\"U7A\",\"assessments\":[],\"subjectFullName\":\"Ge (Geografie) \",\"subjectShortcut\":\"Ge\",\"groupShortcut\":\"7.A\",\"type\":\"REPLACED\",\"furtherInfo\":[{\"name\":\"Učitel\",\"value\":\"<span class=AbsZdroj>Dudová M.<\\/span>\"},{\"name\":\"Třída\",\"value\":\"7.A\"},{\"name\":\"Žáci\",\"value\":\"7.A (celá třída)\"},{\"name\":\"Učebna\",\"value\":\"U7A\"},{\"name\":\"Cyklus\",\"value\":\"bez cyklů\"},{\"name\":\"Den (vyuč. hodina)\",\"value\":\"Pá 24.11. (0)\"},{\"name\":\"Je suplována hodinami\",\"value\":\"Hodina odpadá\"}]},{\"classroomShortcut\":\"U7A\",\"assessments\":[],\"subjectFullName\":\"Aj (Angličtina) \",\"subjectShortcut\":\"Aj\",\"groupShortcut\":\"7.A\",\"type\":\"REGULAR\",\"furtherInfo\":[{\"name\":\"Učitel\",\"value\":\"Šuhajová M.\"},{\"name\":\"Třída\",\"value\":\"7.A\"},{\"name\":\"Žáci\",\"value\":\"7.A (A1)\"},{\"name\":\"Učebna\",\"value\":\"U7A\"},{\"name\":\"Cyklus\",\"value\":\"bez cyklů\"},{\"name\":\"Den (vyuč. hodina)\",\"value\":\"Pá 24.11. (1)\"},{\"name\":\"Probrané učivo\",\"value\":\"Práce s textem (časopis Bridge) - Dávání spropitného v AJ mluvících zemích\"}]},{\"classroomShortcut\":\"PBi\",\"assessments\":[],\"subjectFullName\":\"Bi (Biologie) \",\"subjectShortcut\":\"Bi\",\"groupShortcut\":\"7.A\",\"type\":\"REPLACEMENT\",\"furtherInfo\":[{\"name\":\"Učitel\",\"value\":\"Onderková V.\"},{\"name\":\"Třída\",\"value\":\"7.A\"},{\"name\":\"Žáci\",\"value\":\"7.A (celá třída)\"},{\"name\":\"Učebna\",\"value\":\"PBi\"},{\"name\":\"Cyklus\",\"value\":\"bez cyklů\"},{\"name\":\"Den (vyuč. hodina)\",\"value\":\"Pá 24.11. (2)\"},{\"name\":\"Nahrazuje hodiny\",\"value\":\"Pá 24.11. 2, Bi (Biologie), Dudová M., 7.A (celá třída), PBi\"},{\"name\":\"Probrané učivo\",\"value\":\"Tělní tekutiny-samostatná práce\"}]},{\"classroomShortcut\":\"6.B,\",\"assessments\":[],\"subjectFullName\":\"Vědecký čtyřboj online \",\"subjectShortcut\":\"Vědecký čtyřboj online\",\"groupShortcut\":\"6.A,\",\"type\":\"EVENT\",\"furtherInfo\":[{\"name\":\"Čas výuky\",\"value\":\"10:00 - 14:25\"},{\"name\":\"Učitel\",\"value\":\"Plachtová L.\"},{\"name\":\"Třída\",\"value\":\"6.A, 6.B, 7.A, 7.B, 8.B\"},{\"name\":\"Žáci\",\"value\":\"6.A (seminář), 6.B (seminář), 7.A (seminář), 7.B (seminář), 8.B (seminář)\"},{\"name\":\"Učebna\",\"value\":\"VT1\"},{\"name\":\"Cyklus\",\"value\":\"bez cyklů\"},{\"name\":\"Den (vyuč. hodina)\",\"value\":\"Pá 24.11. (3)\"}]},{\"classroomShortcut\":\"6.B,\",\"assessments\":[],\"subjectFullName\":\"Vědecký čtyřboj online \",\"subjectShortcut\":\"Vědecký čtyřboj online\",\"groupShortcut\":\"6.A,\",\"type\":\"EVENT\",\"furtherInfo\":[{\"name\":\"Čas výuky\",\"value\":\"10:00 - 14:25\"},{\"name\":\"Učitel\",\"value\":\"Plachtová L.\"},{\"name\":\"Třída\",\"value\":\"6.A, 6.B, 7.A, 7.B, 8.B\"},{\"name\":\"Žáci\",\"value\":\"6.A (seminář), 6.B (seminář), 7.A (seminář), 7.B (seminář), 8.B (seminář)\"},{\"name\":\"Učebna\",\"value\":\"VT1\"},{\"name\":\"Cyklus\",\"value\":\"bez cyklů\"},{\"name\":\"Den (vyuč. hodina)\",\"value\":\"Pá 24.11. (3)\"}]},{\"classroomShortcut\":\"6.B,\",\"assessments\":[],\"subjectFullName\":\"Vědecký čtyřboj online \",\"subjectShortcut\":\"Vědecký čtyřboj online\",\"groupShortcut\":\"6.A,\",\"type\":\"EVENT\",\"furtherInfo\":[{\"name\":\"Čas výuky\",\"value\":\"10:00 - 14:25\"},{\"name\":\"Učitel\",\"value\":\"Plachtová L.\"},{\"name\":\"Třída\",\"value\":\"6.A, 6.B, 7.A, 7.B, 8.B\"},{\"name\":\"Žáci\",\"value\":\"6.A (seminář), 6.B (seminář), 7.A (seminář), 7.B (seminář), 8.B (seminář)\"},{\"name\":\"Učebna\",\"value\":\"VT1\"},{\"name\":\"Cyklus\",\"value\":\"bez cyklů\"},{\"name\":\"Den (vyuč. hodina)\",\"value\":\"Pá 24.11. (3)\"}]},{\"classroomShortcut\":\"6.B,\",\"assessments\":[],\"subjectFullName\":\"Vědecký čtyřboj online \",\"subjectShortcut\":\"Vědecký čtyřboj online\",\"groupShortcut\":\"6.A,\",\"type\":\"EVENT\",\"furtherInfo\":[{\"name\":\"Čas výuky\",\"value\":\"10:00 - 14:25\"},{\"name\":\"Učitel\",\"value\":\"Plachtová L.\"},{\"name\":\"Třída\",\"value\":\"6.A, 6.B, 7.A, 7.B, 8.B\"},{\"name\":\"Žáci\",\"value\":\"6.A (seminář), 6.B (seminář), 7.A (seminář), 7.B (seminář), 8.B (seminář)\"},{\"name\":\"Učebna\",\"value\":\"VT1\"},{\"name\":\"Cyklus\",\"value\":\"bez cyklů\"},{\"name\":\"Den (vyuč. hodina)\",\"value\":\"Pá 24.11. (3)\"}]},{\"classroomShortcut\":\"6.B,\",\"assessments\":[],\"subjectFullName\":\"Vědecký čtyřboj online \",\"subjectShortcut\":\"Vědecký čtyřboj online\",\"groupShortcut\":\"6.A,\",\"type\":\"EVENT\",\"furtherInfo\":[{\"name\":\"Čas výuky\",\"value\":\"10:00 - 14:25\"},{\"name\":\"Učitel\",\"value\":\"Plachtová L.\"},{\"name\":\"Třída\",\"value\":\"6.A, 6.B, 7.A, 7.B, 8.B\"},{\"name\":\"Žáci\",\"value\":\"6.A (seminář), 6.B (seminář), 7.A (seminář), 7.B (seminář), 8.B (seminář)\"},{\"name\":\"Učebna\",\"value\":\"VT1\"},{\"name\":\"Cyklus\",\"value\":\"bez cyklů\"},{\"name\":\"Den (vyuč. hodina)\",\"value\":\"Pá 24.11. (3)\"}]},{\"isEmpty\":1},{\"isEmpty\":1},{\"isEmpty\":1}]},{\"lessons\":[{\"isEmpty\":1},{\"isEmpty\":1},{\"classroomShortcut\":\"PBi\",\"assessments\":[],\"subjectFullName\":\"Bi (Biologie) \",\"subjectShortcut\":\"Bi\",\"groupShortcut\":\"7.A\",\"type\":\"REPLACED\",\"furtherInfo\":[{\"name\":\"Učitel\",\"value\":\"<span class=AbsZdroj>Dudová M.<\\/span>\"},{\"name\":\"Třída\",\"value\":\"7.A\"},{\"name\":\"Žáci\",\"value\":\"7.A (celá třída)\"},{\"name\":\"Učebna\",\"value\":\"PBi\"},{\"name\":\"Cyklus\",\"value\":\"bez cyklů\"},{\"name\":\"Den (vyuč. hodina)\",\"value\":\"Pá 24.11. (2)\"},{\"name\":\"Je suplována hodinami\",\"value\":\"Pá 24.11. 2, Bi (Biologie), Onderková V., 7.A (celá třída), PBi\"}]},{\"classroomShortcut\":\"TV1\",\"assessments\":[],\"subjectFullName\":\"Tv\\/c (Tělesná výchova - chlapci) \",\"subjectShortcut\":\"Tv\\/c\",\"groupShortcut\":\"7.A\",\"type\":\"REGULAR\",\"furtherInfo\":[{\"name\":\"Učitel\",\"value\":\"Hýža M.\"},{\"name\":\"Třída\",\"value\":\"7.A\"},{\"name\":\"Žáci\",\"value\":\"7.A (Chlapci)\"},{\"name\":\"Učebna\",\"value\":\"TV1\"},{\"name\":\"Cyklus\",\"value\":\"bez cyklů\"},{\"name\":\"Den (vyuč. hodina)\",\"value\":\"Pá 24.11. (3)\"},{\"name\":\"Probrané učivo\",\"value\":\"Badminton\"}]},{\"classroomShortcut\":\"U7A\",\"assessments\":[],\"subjectFullName\":\"ČjL (Jazyk český) \",\"subjectShortcut\":\"ČjL\",\"groupShortcut\":\"7.A\",\"type\":\"REGULAR\",\"furtherInfo\":[{\"name\":\"Učitel\",\"value\":\"Piňosová R.\"},{\"name\":\"Třída\",\"value\":\"7.A\"},{\"name\":\"Žáci\",\"value\":\"7.A (celá třída)\"},{\"name\":\"Učebna\",\"value\":\"U7A\"},{\"name\":\"Cyklus\",\"value\":\"bez cyklů\"},{\"name\":\"Den (vyuč. hodina)\",\"value\":\"Pá 24.11. (4)\"},{\"name\":\"Probrané učivo\",\"value\":\"Úplné zatmění\"}]},{\"isEmpty\":1},{\"classroomShortcut\":\"U7A\",\"assessments\":[],\"subjectFullName\":\"Šj (Španělština) \",\"subjectShortcut\":\"Šj\",\"groupShortcut\":\"7.A\",\"type\":\"REPLACEMENT\",\"furtherInfo\":[{\"name\":\"Učitel\",\"value\":\"Šuhajová M.\"},{\"name\":\"Třída\",\"value\":\"7.A\"},{\"name\":\"Žáci\",\"value\":\"7.A (Šj)\"},{\"name\":\"Učebna\",\"value\":\"U7A\"},{\"name\":\"Cyklus\",\"value\":\"bez cyklů\"},{\"name\":\"Den (vyuč. hodina)\",\"value\":\"Pá 24.11. (6)\"},{\"name\":\"Nahrazuje hodiny\",\"value\":\"Pá 24.11. 6, Šj (Španělština), <span class=AbsZdroj>Číhalová M.<\\/span>, 7.A (Šj), U8B\"},{\"name\":\"Probrané učivo\",\"value\":\"Minulý čas prostý, slovní zásoba L5\"}]},{\"classroomShortcut\":\"VT3\",\"assessments\":[],\"subjectFullName\":\"D (Dějepis) \",\"subjectShortcut\":\"D\",\"groupShortcut\":\"7.A\",\"type\":\"REPLACED\",\"furtherInfo\":[{\"name\":\"Učitel\",\"value\":\"<span class=AbsZdroj>Skotnicová E.<\\/span>\"},{\"name\":\"Třída\",\"value\":\"7.A\"},{\"name\":\"Žáci\",\"value\":\"7.A (celá třída)\"},{\"name\":\"Učebna\",\"value\":\"VT3\"},{\"name\":\"Cyklus\",\"value\":\"bez cyklů\"},{\"name\":\"Den (vyuč. hodina)\",\"value\":\"Pá 24.11. (7)\"},{\"name\":\"Je suplována hodinami\",\"value\":\"Hodina odpadá\"}]},{\"isEmpty\":1},{\"isEmpty\":1},{\"isEmpty\":1}]},{\"lessons\":[{\"isEmpty\":1},{\"isEmpty\":1},{\"isEmpty\":1},{\"isEmpty\":1},{\"isEmpty\":1},{\"isEmpty\":1},{\"classroomShortcut\":\"U8B\",\"assessments\":[],\"subjectFullName\":\"Šj (Španělština) \",\"subjectShortcut\":\"Šj\",\"groupShortcut\":\"7.A\",\"type\":\"REPLACED\",\"furtherInfo\":[{\"name\":\"Učitel\",\"value\":\"<span class=AbsZdroj>Číhalová M.<\\/span>\"},{\"name\":\"Třída\",\"value\":\"7.A\"},{\"name\":\"Žáci\",\"value\":\"7.A (Šj)\"},{\"name\":\"Učebna\",\"value\":\"U8B\"},{\"name\":\"Cyklus\",\"value\":\"bez cyklů\"},{\"name\":\"Den (vyuč. hodina)\",\"value\":\"Pá 24.11. (6)\"},{\"name\":\"Je suplována hodinami\",\"value\":\"Pá 24.11. 6, Šj (Španělština), Šuhajová M., 7.A (Šj), U7A\"}]},{\"isEmpty\":1},{\"isEmpty\":1},{\"isEmpty\":1},{\"isEmpty\":1}]}]}]}");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        System.out.println("here: " + timeTable.days[0].lessonRows[0].lessons[2].subjectFullName);
        System.out.println("-----------timetable start------------------");
        timeTable.printSelf();
        System.out.println("-----------timetable end------------------");*/

        if (n == 0) {
            textview.setText("Nepřečtené správy:  0");
        }

        /*View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Open_activity_button_info();
                System.out.println("safafafa");
            }
        };

        for (final Button button : buttonsArrayList) {
            button.setOnClickListener(listener);
        }*/


        //Open_activity_button_info();
        //buttonInfoOpener();

        startActivity(new Intent(this, EventsActivity.class));
        finish();

    }

    public static MainActivity getInstance() {
        return instance;
    }

    public int Buttonbutton(){
        // Create Button
        button = new Button(this);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linerLayout);
        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollview);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);

        button.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                160
        ));

        if(n >= 0) {
            n++;
        }

        Typeface typeface = ResourcesCompat.getFont(this, R.font.nexa_heavy_bold_font);
        button.setTypeface(typeface);
        button.setText("Button " + n);
        button.setClickable(true);
        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{Color.parseColor("#6922b5"), Color.parseColor("#2e0659")}
        );

        gradientDrawable.setCornerRadius(20); // Set corner radius if needed
        button.setBackground(gradientDrawable);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) button.getLayoutParams();
        params.setMargins(33, 40, 33, 0); // Set margins
        button.setLayoutParams(params);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Open_activity_button_info();
                System.out.println("fungujee");
            }
        });
        buttonsArrayList.add(button);

        //addButtonToArray(button, buttonsArrayList);
        //addButtonsToList();

        if (n == 5) {
            System.out.println(buttonsArrayList);
        }

        linearLayout.addView(button);

        setContentView(relativeLayout);
        System.out.println("vytvarim button");

        Typeface NprctnSprvFont = ResourcesCompat.getFont(this, R.font.nexa_heavy_bold_font);
        textview.setTypeface(NprctnSprvFont);
        textview.setText("Nepřečtené správy:  " + n);

        return n;
    }
    public static ArrayList<Button> addButtonToArray(Button buttonn, ArrayList<Button> araylist) {
        araylist.add(buttonn);
        return araylist;

    }

    public void addButtonArray(Button button) {
        buttonsArrayList.add(button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Open_activity_button_info();
                System.out.println("fungujee");
            }
        });
    }

    public void addButtonsToList() {
        // Code to create and add buttons to the layout and array list
        Button newButton = new Button(this);
        // Add newButton to your layout
        addButtonArray(newButton);
    }


    /*public void buttonInfoOpener (final Button btn, ArrayList<Button> BtnArraylist){
        for (btn : BtnArraylist) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Open_activity_button_info();
                    System.out.println("fakujuuuuu");
                }
            });
        }
    }*/



    public void Open_activity_button_info() {
        Intent intentt = new Intent(this, button_info.class);
        startActivity(intentt);
    }

    public void Open_activity_new_event() {
        Intent intent = new Intent(this, event_new_event.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        // Handle your menu item clicks here
        return super.onOptionsItemSelected(item);
    }
}