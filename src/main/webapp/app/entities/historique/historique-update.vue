<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="pfaApp.historique.home.createOrEditLabel"
          data-cy="HistoriqueCreateUpdateHeading"
          v-text="$t('pfaApp.historique.home.createOrEditLabel')"
        >
          Create or edit a Historique
        </h2>
        <div>
          <div class="form-group" v-if="historique.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="historique.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('pfaApp.historique.dateArosage')" for="historique-dateArosage">Date Arosage</label>
            <input
              type="text"
              class="form-control"
              name="dateArosage"
              id="historique-dateArosage"
              data-cy="dateArosage"
              :class="{ valid: !$v.historique.dateArosage.$invalid, invalid: $v.historique.dateArosage.$invalid }"
              v-model="$v.historique.dateArosage.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('pfaApp.historique.qttEau')" for="historique-qttEau">Qtt Eau</label>
            <input
              type="number"
              class="form-control"
              name="qttEau"
              id="historique-qttEau"
              data-cy="qttEau"
              :class="{ valid: !$v.historique.qttEau.$invalid, invalid: $v.historique.qttEau.$invalid }"
              v-model.number="$v.historique.qttEau.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('pfaApp.historique.parcelle')" for="historique-parcelle">Parcelle</label>
            <select class="form-control" id="historique-parcelle" data-cy="parcelle" name="parcelle" v-model="historique.parcelle">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="historique.parcelle && parcelleOption.id === historique.parcelle.id ? historique.parcelle : parcelleOption"
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
            :disabled="$v.historique.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./historique-update.component.ts"></script>
