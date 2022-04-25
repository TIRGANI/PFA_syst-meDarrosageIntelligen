<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="pfaApp.affectation.home.createOrEditLabel"
          data-cy="AffectationCreateUpdateHeading"
          v-text="$t('pfaApp.affectation.home.createOrEditLabel')"
        >
          Create or edit a Affectation
        </h2>
        <div>
          <div class="form-group" v-if="affectation.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="affectation.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('pfaApp.affectation.dateDebut')" for="affectation-dateDebut">Date Debut</label>
            <input
              type="text"
              class="form-control"
              name="dateDebut"
              id="affectation-dateDebut"
              data-cy="dateDebut"
              :class="{ valid: !$v.affectation.dateDebut.$invalid, invalid: $v.affectation.dateDebut.$invalid }"
              v-model="$v.affectation.dateDebut.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('pfaApp.affectation.dateFin')" for="affectation-dateFin">Date Fin</label>
            <input
              type="text"
              class="form-control"
              name="dateFin"
              id="affectation-dateFin"
              data-cy="dateFin"
              :class="{ valid: !$v.affectation.dateFin.$invalid, invalid: $v.affectation.dateFin.$invalid }"
              v-model="$v.affectation.dateFin.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('pfaApp.affectation.boitier')" for="affectation-boitier">Boitier</label>
            <select class="form-control" id="affectation-boitier" data-cy="boitier" name="boitier" v-model="affectation.boitier">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="affectation.boitier && boitierOption.id === affectation.boitier.id ? affectation.boitier : boitierOption"
                v-for="boitierOption in boitiers"
                :key="boitierOption.id"
              >
                {{ boitierOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('pfaApp.affectation.parcelle')" for="affectation-parcelle">Parcelle</label>
            <select class="form-control" id="affectation-parcelle" data-cy="parcelle" name="parcelle" v-model="affectation.parcelle">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="affectation.parcelle && parcelleOption.id === affectation.parcelle.id ? affectation.parcelle : parcelleOption"
                v-for="parcelleOption in parcelles"
                :key="parcelleOption.id"
              >
                {{ parcelleOption.id }}
              </option>
            </select>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.cancel')">Cancel</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="$v.affectation.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./affectation-update.component.ts"></script>
