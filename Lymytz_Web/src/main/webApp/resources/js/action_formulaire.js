/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function valueChange(suffix, noeud) {
    if (noeud.val().length > 0) {
        $('#cancel_' + suffix).find('.ui-button-text').html('Annuler');
    } else {
        $('#cancel_' + suffix).find('.ui-button-text').html('Nouveau');
    }
}

function initRegle_() {
    $('.signes_op').css('cursor', 'pointer');
}


/******************************BULLETINS DE PAYE*****************************/

function choixHeader_() {
    $("#refAction").val('N');     //indique  qu'on veux créer un en-tête normale pour les bulletins

}

function choixHeader() {
    dlgOrdreCalcul.show();
    $("#refAction").val('P');   //indique  qu'on veux planifier l'ordre
}


/*******************CONTRAT******************************/
function opendlgAddPreavis(value, i1, i2) {
    dlgAddpreavis.show();
    $("#inputValIntervale").val(value);
    $("#inputValCintervale").val(value);
    $(".ii1").text(i1);
    $(".ii2").text(i2);
}


/*******************PRESENCE******************************/

function openDlgValidePointage(noeud) {
    if (noeud.is(':checked')) {
        dlgConfirmValid.show();
    } else {
        dlgConfirmInValid.show();
    }
}
var displayMarge = false;
function displayChampUpdateMarge() {
    if (displayMarge) {
        $(".displayMarge").show();
        displayMarge = false;
    } else {
        $(".displayMarge").hide();
        displayMarge = true;
    }
}
var displayHs = false;
function displayChampUpdateHs() {
    if (displayHs) {
        $(".displayHs").show();
        displayHs = false;
    } else {
        $(".displayHs").hide();
        displayHs = true;
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


/*Formulaire de gestion des congés*/

function choixNatureAbscence() {
    var v = $('input[type=radio][name=selectNaturePerm]:checked').attr('value');
    if (v === 'P') {
        $('.conge_champDuree').css('display', 'inline');
        $('.conge_typeConge').css('display', 'none');
        if ($('input[type=radio][name=selectTypeDuree]:checked').attr('value') === 'C') {
            $('.conge_label_heure, .conge_endDate, .conge_label_heure_').css('display', 'none');
            $('.conge_labelDuree').text('Durée (en heure)');
        }
    } else {
        $('.conge_label_heure, .conge_startDate, .conge_endDate, .conge_label_heure_').css('display', 'inline');
        $('.conge_champDuree').css('display', 'none');
        $('.conge_typeConge').css('display', 'inline');
    }
}

function choixDureeAbscence() {
    var v = $('input[type=radio][name=selectTypeDuree]:checked').attr('value');
    if (v === 'C') {
        //si c'est une permission à courte durée, on cache les champs dates
        $('.conge_label_dateDebut').text('Heure de début');
        $('.conge_label_dateFin').text('Heure de Fin');
        $('.conge_label_heure, .conge_endDate, .conge_label_heure_').css('display', 'none');
        //modifie le label de la durée
        $('.conge_labelDuree').text('Durée (en heure)');

    } else {
        $('.conge_label_dateDebut').text('Date de début');
        $('.conge_label_dateFin').text('Date de Fin');
        $('.conge_label_heure, .conge_startDate, .conge_endDate, .conge_label_heure_').css('display', 'inline');
        //modifie le label de la durée
        $('.conge_labelDuree').text('Durée (en jour)');
        $("#txt_date_paiement_conge").css('display', 'none');
    }
}

function conge_giveFocus() {
    $("#chp_descriptionCOnge").focus();
    choixDureeAbscence();
}


/*Vue contrat*/

//choix du la source de la date d'evaluation du premier congé
function choixParamFirstConge(node) {
    var val = node.val();
    if (val === 'DF') {
        $(".paramDateF").css('display', 'inline');
    } else {
        $(".paramDateF").css('display', 'none');
    }
}