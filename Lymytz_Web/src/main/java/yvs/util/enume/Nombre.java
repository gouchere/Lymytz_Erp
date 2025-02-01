package yvs.util.enume;

public enum Nombre {

    //nombre simple
//nombre simple
    ZERO(0, "zero"), UN(1, "un"), DEUX(2, "deux"), TROIS(3, "trois"),
    QUATRE(4, "quatre"), CINQ(5, "cinq"), SIX(6, "six"), SEPT(7, "sept"),
    HUIT(8, "huit"), NEUF(9, "neuf"), DIX(10, "dix"), ONZE(11, "onze"),
    DOUZE(12, "douze"), TREIZE(13, "treize"), QUATORZE(14, "quatorze"),
    QUINZE(15, "quinze"), SEIZE(16, "seize"), DIXSEPT(17, "dix-sept"),
    DIXHUIT(18, "dix-huit"), DIXNEUF(19, "dix-neuf"),
    //de 20 à 99
    VINGT(20, 29, "vingt"),
    TRENTE(30, 39, "trente"),
    QUARANTE(40, 49, "quarante"),
    CINQUANTE(50, 59, "cinquante"),
    SOIXANTE(60, 69, "soixante"),
    SOIXANTEDIX(70, 79, "soixante-dix", SOIXANTE),
    QUATREVINGT(80, 89, "quatre-vingt", "s"),
    QUATREVINGTDIX(90, 99, "quatre-vingt-dix", QUATREVINGT),
    //de 10 à X milliard
    DIXAINE(10, 99),
    CENT(100, 999, "cent", DIXAINE),
    MILLE(1000, 999999, "mille", CENT),
    MILLION(1000000, 99999999, "million", MILLE),
    MILLIARD(1000000000, Long.MAX_VALUE, "milliard", MILLION),
    //enum de calcul
    CALCULATE() {
                @Override
                public String getValue(long value) throws Exception {
                    if (value == 0) {
                        return ZERO.label;
                    } else {
                        return ((value < 0) ? "moins " : "") + MILLIARD.getStringValue((Math.abs(value)));
                    }
                }

                @Override
                public String getValue(double value) throws Exception {
                    if (value == 0) {
                        return ZERO.label;
                    } else {
                        return ((value < 0) ? "moins " : "") + MILLIARD.getStringValue((long)(Math.abs(value)));
                    }
                }

                @Override
                public String getValue(double value, String separator) throws Exception {
                    if (value == 0) {
                        return ZERO.label + " " + separator;
                    } else {
                        StringBuilder sb = new StringBuilder();
                        sb.append((value < 0) ? "moins " : "");
                        String vstr = Double.toString(value);

                        int indexOf = vstr.indexOf('.');

                        if (indexOf == -1) {
                            sb.append(MILLIARD.getStringValue((long) (Math.abs(value))));
                            sb.append(" ");
                            sb.append(separator);
                        } else {
                            sb.append(MILLIARD.getStringValue(Long.parseLong(vstr.substring(0, indexOf))));
                            sb.append(" ");
                            sb.append(separator);
                            String floatting = vstr.substring(indexOf + 1, (indexOf + 3 >= vstr.length()) ? vstr.length() : indexOf + 3) + (indexOf + 3 >= vstr.length() ? "0" : "");
                            long v = Long.parseLong(floatting);
                            if (v != 0) {
                                sb.append(" ");
                                sb.append(MILLIARD.getStringValue(v));
                            }
                        }
                        return sb.toString();
                    }
                }

            };

    protected long min, max;
    protected String label;
    protected Nombre before;
    // valeur à ajout à la fin d'un nombre entier
    private String addMin;
    /* constructeurs*/

    Nombre() {
    }

    Nombre(long v, String s) {
        this(v, v, s);
    }

    Nombre(long min, long max) {
        this.min = min;
        this.max = max;
    }

    Nombre(long min, long max, String label, Nombre before) {
        this(min, max, label);
        this.before = before;
    }

    Nombre(long min, long max, String label, String addMin) {
        this(min, max, label);
        this.addMin = addMin;
    }

    Nombre(long min, long max, String label) {
        this(min, max);
        this.label = label;
    }

    public String getValue(long value) throws Exception {
        throw new Exception("Vous devez appeller la m�thode par l'�num�ration Chiffre.CALCULATE");
    }

    public String getValue(double value) throws Exception {
        throw new Exception("Vous devez appeller la m�thode par l'�num�ration Chiffre.CALCULATE");
    }

    public String getValue(double value, String separator) throws Exception {
        throw new Exception("Vous devez appeller la m�thode par l'�num�ration Chiffre.CALCULATE");
    }

    // fonction de transformation
    private String getStringValue(long value) {
        long v1 = value / this.min;
        if (v1 == 0 && before != null) {
            return before.getStringValue(value);
        }

        StringBuilder add = new StringBuilder();
        Nombre[] values = Nombre.values();
        if (value < 20) {
            return values[(int) value].label;
        }
        for (int i = 0; i < values.length; i++) {
            Nombre nombre = values[i];
            //si la valeur est inférieur à 100
            if (value < 100 && nombre.min <= value && nombre.max >= value) {
                //cas des valeurs 20, 30, 40, etc...
                if (value == nombre.min) {
                    return nombre.label + ((nombre.addMin != null) ? nombre.addMin : "");
                } //cas de 71->79 et 91->99
                else if (nombre.before != null) {
                    StringBuilder sb = new StringBuilder(nombre.before.label);
                    //71
                    sb.append(((value - nombre.min + 10 == 11 && nombre.equals(SOIXANTEDIX)) ? " et " : "-"));
                    sb.append(DIXAINE.getStringValue(value - nombre.min + 10));
                    return sb.toString();
                } else {
                    StringBuilder sb = new StringBuilder(nombre.label);
                    //81
                    sb.append(((value - nombre.min == 1 && !nombre.equals(QUATREVINGT)) ? " et " : "-"));
                    //second chiffre
                    sb.append(((value - nombre.min > 0) ? DIXAINE.getStringValue(value - nombre.min) : ""));
                    return sb.toString();
                }
            } else if (nombre.min <= v1 && nombre.max >= v1 && value >= 100) {
				//première partie du nombre

                //100 et 1000
                if ((this.equals(MILLE) || this.equals(CENT)) && Nombre.UN.equals(nombre)) {
                    add.append(label);
                } else {
                    add.append(nombre.getStringValue(v1));
                    //cas : Million de millard et Milliard de milliard 
                    add.append(((MILLIARD.equals(this) && (MILLION.equals(nombre) || MILLIARD.equals(nombre)) ? " de" : "")));
                    //ajout du label si pr�sent
                    add.append(((label != null) ? " " + label : ""));
                }
                //deuxième partie du nombre
                add.append(((value - (v1 * this.min) > 0) ? (" " + before.getStringValue(value - (v1 * this.min))) : (v1 > 1) ? "s" : ""));
                return add.toString();
            }
        }
        return add.toString();
    }
}
