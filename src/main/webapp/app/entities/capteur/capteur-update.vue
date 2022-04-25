<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="pfaApp.capteur.home.createOrEditLabel"
          data-cy="CapteurCreateUpdateHeading"
          v-text="$t('pfaApp.capteur.home.createOrEditLabel')"
        >
          Create or edit a Capteur
        </h2>
        <div>
          <div class="form-group" v-if="capteur.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="capteur.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('pfaApp.capteur.type')" for="capteur-type">Type</label>
            <input
              type="text"
              class="form-control"
              name="type"
              id="capteur-type"
              data-cy="type"
              :class="{ valid: !$v.capteur.type.$invalid, invalid: $v.capteur.type.$invalid }"
              v-model="$v.capteur.type.$model"
              required
            />
            <div v-if="$v.capteur.type.$anyDirty && $v.capteur.type.$invalid">
              <small class="form-text text-danger" v-if="!$v.capteur.type.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('pfaApp.capteur.image')" for="capteur-image">Image</label>
            <input
              type="text"
              class="form-control"
              name="image"
              id="capteur-image"
              data-cy="image"
              :class="{ valid: !$v.capteur.image.$invalid, invalid: $v.capteur.image.$invalid }"
              v-model="$v.capteur.image.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('pfaApp.capteur.description')" for="capteur-description">Description</label>
            <input
              type="text"
              class="form-control"
              name="description"
              id="capteur-description"
              data-cy="description"
              :class="{ valid: !$v.capteur.description.$invalid, invalid: $v.capteur.description.$invalid }"
              v-model="$v.capteur.description.$model"
            />
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
            :disabled="$v.capteur.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./capteur-update.component.ts"></script>
