<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="pfaApp.brache.home.createOrEditLabel"
          data-cy="BracheCreateUpdateHeading"
          v-text="$t('pfaApp.brache.home.createOrEditLabel')"
        >
          Create or edit a Brache
        </h2>
        <div>
          <div class="form-group" v-if="brache.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="brache.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('pfaApp.brache.branche')" for="brache-branche">Branche</label>
            <input
              type="number"
              class="form-control"
              name="branche"
              id="brache-branche"
              data-cy="branche"
              :class="{ valid: !$v.brache.branche.$invalid, invalid: $v.brache.branche.$invalid }"
              v-model.number="$v.brache.branche.$model"
              required
            />
            <div v-if="$v.brache.branche.$anyDirty && $v.brache.branche.$invalid">
              <small class="form-text text-danger" v-if="!$v.brache.branche.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small class="form-text text-danger" v-if="!$v.brache.branche.numeric" v-text="$t('entity.validation.number')">
                This field should be a number.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label v-text="$t('pfaApp.brache.capteur')" for="brache-capteur">Capteur</label>
            <select
              class="form-control"
              id="brache-capteurs"
              data-cy="capteur"
              multiple
              name="capteur"
              v-if="brache.capteurs !== undefined"
              v-model="brache.capteurs"
            >
              <option v-bind:value="getSelected(brache.capteurs, capteurOption)" v-for="capteurOption in capteurs" :key="capteurOption.id">
                {{ capteurOption.id }}
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
            :disabled="$v.brache.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./brache-update.component.ts"></script>
