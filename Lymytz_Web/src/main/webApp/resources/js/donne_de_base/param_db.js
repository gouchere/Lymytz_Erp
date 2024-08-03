/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(function() {
    reseize();
});
function reseize() {
    //alert($('body').find('.part_fix').height());
    $('body').find('.part_scroll').height($('body').height() - $('body').find('.part_fix').height() - 150);
}
function resetHide(val) {
    $('#' + val).val('');
}

function hideElement(element) {
    $('#' + element).css('display', 'none');
}

function showElement(element) {
    $('#' + element).css('display', 'inline');
}

function write(val) {
//    console.log(val);
}

/*********************************************USERS******************************************/



/************************************************************/
/**Gère la sélection des éléments d'un tableau**/

var nbSelectionTab = 0;

function selectionAllLine2Tab(nbreLigne, suffix1, suffix2) {
    selectionAllLineTab(nbreLigne, suffix1);
    selectionAllLineTab(nbreLigne, suffix2);
}

function selectionAllLineTab(nbreLigne, suffix) {
    if ($('.chek_all_line_' + suffix).is(':checked')) {
        $(".chek_line_" + suffix).each(function() {
            if (!$(this).is(':checked')) {
                $(this).trigger('click');
            }
        });
    } else {
        var val = "";
        nbreLigne = 0;
        $('.chek_line_' + suffix).attr('checked', false);
        $('#input_hide_' + suffix).val("");
    }

    if (nbreLigne > 0) {
        $('.nbSelect' + suffix).text(nbreLigne + " Eléménts sélectioné");
    } else {
        $('.nbSelect' + suffix).text("");
    }
    setNbreSelectionTab(nbreLigne, suffix);
}

function selectionLine2Tab(numLine, suffix1, suffix2) {
    selectionLineTab(numLine, suffix1);
    selectionLineTab(numLine, suffix2);
}

function onselectLine(numLine, suffix) {
    var val = $('#input_hide_' + suffix).val();
    var tab = val.split('-');
    $(".chek_line_" + suffix).each(function() {
        if ($(this).is(':checked')) {
            $(this).trigger('click');
        }
    });
}
function selectionLineTab(numLine, suffix) {
    var val = $('#input_hide_' + suffix).val();
    var tab = val.split('-');
    var num = 0;
    var i = 0;
    var add = true;
    for (i = 0; i < tab.length; i++) {
        num = tab[i];
        if (parseInt(num) === numLine) {
            tab[i] = null;
            add = false;
            break;
        }
    }
    if (add) {
        val += numLine + "-";
    } else {
        numLine = "-" + numLine + "-";
        val = Remplace("-" + val, numLine, "-");
    }
    tab = val.split('-');
    val = "";
    nbSelectionTab = 0;
    for (i = 0; i < tab.length; i++) {
        num = tab[i];
        if (num !== "") {
            val += num + "-";
            nbSelectionTab++;
        }
    }
    $('#input_hide_' + suffix).val(val);
    if (nbSelectionTab > 0) {
        $('.nbSelect' + suffix).text(nbSelectionTab + " Elémént(s) sélectioné(s)");
    } else {
        $('.nbSelect' + suffix).text("");
    }
    setNbreSelectionTab(nbSelectionTab, suffix);
}

function selectionLineTab_(numLine, suffix) {
    $('.chek_line_' + suffix).eq(numLine).trigger('click');        //    
}

function setNbreSelectionTab(nbre_, suffix) {
    nbSelectionTab = parseInt(nbre_);
    selectionLineTable(suffix);
}

function selectionLineTable(suffix) {
    if (nbSelectionTab === 1) {
        $('#delete_' + suffix).css('display', 'inline');
        $('#option_' + suffix).css('display', 'inline');
        $('#update_' + suffix).css('display', 'inline');
        $('#other_' + suffix).css('display', 'inline');
    } else {
        $('#delete_' + suffix).css('display', 'none');
        $('#option_' + suffix).css('display', 'none');
        $('#update_' + suffix).css('display', 'none');
        $('#other_' + suffix).css('display', 'none');
    }
    if (nbSelectionTab > 1) {
        $('#delete_' + suffix).css('display', 'inline');
        $('#option_' + suffix).css('display', 'inline');
        $('#update_' + suffix).css('display', 'none');
        $('#other_' + suffix).css('display', 'inline');
    }
}








function valueChange(suffix, noeud) {
    if (noeud.val().length > 0) {
        $('#cancel_' + suffix).find('.ui-button-text').html('Annuler');
    } else {
        $('#cancel_' + suffix).find('.ui-button-text').html('Nouveau');
    }
}

function openDialog(wdgt, noeud) {
    if (noeud.val() === '-1') {
        wdgt.show();

    }
}

function hideDialog(wdgt) {
    wdgt.hide();
}

/***************************AGENCE*************************/

function choixVille_agence(noeud) {
    if (noeud.val() === '-1') {
        dlgVille.show();
    }
}

function choixSecteurActivite(noeud) {
    if (noeud.val() === '-1') {
        dlgSecteurActivite.show();
    }
}




/***********************************DEPARTEMENT*********************************/

function choixDepParent(noeud) {
    if (noeud.val() === '-1') {
        dlgDepartement.show();
    }

}
function choixResponsableDepartement(noeud) {
    if (noeud.val() === '-1') {
        dlgEmploye.show();
    }

}






/**************************POSTE DE TRAVAIL*******************************/
function choixDepartementPoste(noeud) {
    if (noeud.val() === '-1') {
        dlgDepartement.show();
    }
}


function choixQualificationPoste(noeud) {
    if (noeud.val() === '-1') {
        dlgQualification.show();
    }
}




/************************EMPLOYE***********************************/

function choixGradeEmploye(noeud) {
    if (noeud.val() === '-1') {
        dlgAddGrade.show();
    }
}
function choixVilleEmploye(noeud) {
    if (noeud.val() === '-1') {
        dlgVilleCNI.show();
    }
}
function choixPosteEmploye(noeud) {
    if (noeud.val() === '-1') {
        dlgPosteDeTravail.show();
    }
}

function choixQualificationEmploye(noeud) {
    if (noeud.val() === '-1') {
        dlgQualification.show();
    }
}


function choixLangueEmploye(noeud) {
    if (noeud.val() === '-1') {
        dlgLangue.show();
    }
}



function choixLieux_Emp(noeud) {
    if (noeud.val() === '-1') {
        dlgVille.show();
    }
}


/*********************************CONTRAT***********************************/
function choixTypeContrat(noeud) {
    if (noeud.val() === '-1') {
        dlgAddTypeContrat.show();
    }
}

function choixModePaiement(noeud) {
    if (noeud.val() === '-1') {
        dlgAddTypeModePaiement.show();
    }
}

function choixTypePrime(noeud) {
    if (noeud.val() === '-1') {
        dlgAddTypePrime.show();
        collapseList('contratTypePrime');
    }
}






/***************************REGLE DE SALAIRE**************************/
function choixCategorieRegle(noeud) {
    if (noeud.val() === '-1') {
        dlgListCategorie.show();
    }
}

function initRegle_() {
    $('.signes_op').css('cursor', 'pointer');
}


function completeExpr(v) {
    var chaine = $('#txarea-expr').val();
    chaine = chaine + " " + v;
    $('#txarea-expr').val(chaine);
}

function choixBasePourcentage(noeud) {
    if (noeud.val() === '-1') {
        dlgListRegle1.show();
    }
}
function choixBaseQte(noeud) {
    if (noeud.val() === '-1') {
        dlgListRegle.show();
    }
}
function choixRubrique(noeud) {
    if (noeud.val() === '-1') {
        dlgRubrique.show();
    }
}

/******************************BULLETINS DE PAYE*****************************/

function choixHeader_(noeud) {
    if (noeud.val() === '-1') {
        dlgOrdreCalcul.show();
        $("#refAction").val('N');     //indique  qu'on veux créer un en-tête normale pour les bulletins
    }
}

function choixHeader() {
    dlgOrdreCalcul.show();
    $("#refAction").val('P');   //indique  qu'on veux planifier l'ordre
}


/******************************AFFECTATION EMPLOYES*****************************/

function openDlgEmploye_affectation() {
    dlgEmploye.show();
    collapseGrid('employeAffectation');
}


/******************************NIVEAU d'ACCES*****************************/

function choixNiveau_acces(noeud) {
    if (noeud.val() === '-1') {
        dlgNiveauAcces.show();
    }
}


/******************************CATEGORIE*****************************/

function choixduree_preavis() {
    dlgAddDuree.show();
}


function opendlgAddPreavis(value, i1, i2) {
    dlgAddpreavis.show();
    $("#inputValIntervale").val(value);
    $("#inputValCintervale").val(value);
    $(".ii1").text(i1);
    $(".ii2").text(i2);
}




/*******************PRESENCE******************************/
function choix_modeValidation(noeud) {
    if (noeud.val() === '-1') {
        dlgAddTV.show();
    }
}
function openDlgValidePointage(noeud) {
    if (noeud.is(':checked')) {
        dlgConfirmValid.show();
    } else {
        dlgConfirmInValid.show();
    }
}

function hideHistorique() {
    $('.historique_presence').css('display', 'none');
}
function showHistorique() {
    $('.historique_presence').css('display', 'block');
    collapseForm('presenceEmps');
    $('.yvs_form_presenceEmps').css('display', 'none');
}


/**************RETENUES EMPLOYE**********************/
function choix_typeRetenuEmploye(noeud) {
    if (noeud.val() === '-1') {
        collapseForm('retenueTypeR');
        dlgRetenu.show();
    }
}

/********************CONGES******************************/

function changeValueFilter() {
    $('.conge_list').css('display', 'block');
    $('.conge_grid').css('display', 'none');
}

/*----------PARAMETRE GENERAUX-----------------------*/
function choixModeAvancement(noeud) {
    if (noeud.val() === 'false') {
        $('.reclassement').css('display', 'none');
    } else {
        $('.reclassement').css('display', 'inline');
    }
}
function choixModeAvancement_() {
    var noeud = $("#txt_reclassement");
    choixModeAvancement(noeud);
}

/********************Planing de travail********************/
function changeViewPlaningWork() {
    var str = $('.tabPlaning').attr('display');
    if (str === 'ON') {
        $('.tabPlaning').css('display', 'block');
        $('.gridPlaning').css('display', 'none');
        $('.tabPlaning').attr('display', 'OFF');
    } else {
        $('.tabPlaning').css('display', 'none');
        $('.gridPlaning').css('display', 'block');
        $('.tabPlaning').attr('display', 'ON');
    }
}
