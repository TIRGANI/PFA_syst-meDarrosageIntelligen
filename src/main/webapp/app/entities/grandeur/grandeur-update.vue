<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="pfaApp.grandeur.home.createOrEditLabel"
          data-cy="GrandeurCreateUpdateHeading"
          v-text="$t('pfaApp.grandeur.home.createOrEditLabel')"
        >
          Create or edit a Grandeur
        </h2>
        <div>
          <div class="form-group" v-if="grandeur.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="grandeur.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('pfaApp.grandeur.type')" for="grandeur-type">Type</label>
            <input
              type="text"
              class="form-control"
              name="type"
              id="grandeur-type"
              data-cy="type"
              :class="{ valid: !$v.grandeur.type.$invalid, invalid: $v.grandeur.type.$invalid }"
              v-model="$v.grandeur.type.$model"
              required
            />
            <div v-if="$v.grandeur.type.$anyDirty && $v.grandeur.type.$invalid">
              <small class="form-text text-danger" v-if="!$v.grandeur.type.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('pfaApp.grandeur.valeur')" for="grandeur-valeur">Valeur</label>
            <input
              type="text"
              class="form-control"
              name="valeur"
              id="grandeur-valeur"
              data-cy="valeur"
              :class="{ valid: !$v.grandeur.valeur.$invalid, invalid: $v.grandeur.valeur.$invalid }"
              v-model="$v.grandeur.valeur.$model"
              required
            />
            <div v-if="$v.grandeur.valeur.$anyDirty && $v.grandeur.valeur.$invalid">
              <small class="form-text text-danger" v-if="!$v.grandeur.valeur.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('pfaApp.grandeur.date')" for="grandeur-date">Date</label>
            <input
              type="text"
              class="form-control"
              name="date"
              id="grandeur-date"
              data-cy="date"
              :class="{ valid: !$v.grandeur.date.$invalid, invalid: $v.grandeur.date.$invalid }"
              v-model="$v.grandeur.date.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('pfaApp.grandeur.parcelle')" for="grandeur-parcelle">Parcelle</label>
            <select class="form-control" id="grandeur-parcelle" data-cy="parcelle" name="parcelle" v-model="grandeur.parcelle">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="grandeur.parcelle && parcelleOption.id === grandeur.parcelle.id ? grandeur.parcelle : parcelleOption"
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
            :disabled="$v.grandeur.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./grandeur-update.component.ts"></script>
