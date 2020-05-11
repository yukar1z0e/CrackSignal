package com.yukar0z0e.cracksignal;


import android.app.Application;
import android.content.Context;
import android.os.FileObserver;
import android.os.Handler;
import android.os.Parcel;
import android.util.Log;
import android.view.View;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.MotionEvent;

import java.lang.reflect.Array;
import java.text.Format;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.Objects;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.assetAsByteArray;
import static de.robv.android.xposed.XposedHelpers.findAndHookConstructor;
import static de.robv.android.xposed.XposedHelpers.findClass;
import static de.robv.android.xposed.XposedHelpers.findField;
import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.getByteField;
import static de.robv.android.xposed.XposedHelpers.newInstance;
import static de.robv.android.xposed.XposedHelpers.setObjectField;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class crackmain implements IXposedHookLoadPackage {
    private XC_LoadPackage.LoadPackageParam lpparam = null;

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (loadPackageParam.packageName.contains("org.thoughtcrime.securesms")) {
            findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    lpparam = loadPackageParam;
                    Log.d("crackSignal", "cracked Signal App");
                    //getAddress();
                    //hookAddressFunction();
                    //hookContext();
                    //decrypt();
                    sender();
                }
            });
        }
    }

    public void getAddress() {
        final Class<?> MessageSenderClass = findClass("org.thoughtcrime.securesms.sms.MessageSender", lpparam.classLoader);
        final Class<?> RecipientClass = findClass("org.thoughtcrime.securesms.recipients.Recipient", lpparam.classLoader);
        final Class<?> AddressClass = findClass("org.thoughtcrime.securesms.database.Address", lpparam.classLoader);

        findAndHookMethod(MessageSenderClass, "sendTextMessage", Context.class, RecipientClass, boolean.class, boolean.class, long.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Field nameRecipentField = findField(RecipientClass, "name");
                Field customLabelRecipentField = findField(RecipientClass, "customLabel");
                Field systemContactPhotoRecipentField = findField(RecipientClass, "systemContactPhoto");
                Field contactUriRecipentField = findField(RecipientClass, "contactUri");
                Field profileKeyRecipentField = findField(RecipientClass, "profileKey");
                Field profileNameRecipentField = findField(RecipientClass, "profileName");
                Field profileAvatarRecipentField = findField(RecipientClass, "profileAvatar");
                Field addressRecipentField = findField(RecipientClass, "address");
                Object addressRecipentObject = addressRecipentField.get(param.args[0]);

                Field addressAddressField = findField(AddressClass, "address");

                Log.d(" crackSignal", "name: " + nameRecipentField.get(param.args[0])
                        + " customLabel: " + customLabelRecipentField.get(param.args[0])
                        + " systemContactPhoto: " + systemContactPhotoRecipentField.get(param.args[0])
                        + " contactUri: " + contactUriRecipentField.get(param.args[0])
                        + " profileKey: " + profileKeyRecipentField.get(param.args[0])
                        + " profileName: " + profileNameRecipentField.get(param.args[0])
                        + " profileAvatar: " + profileAvatarRecipentField.get(param.args[0])
                        + " address: " + addressAddressField.get(addressRecipentObject)
                );
            }
        });
    }

    public void hookAddressFunction() {
        final Class<?> AddressClass = findClass("org.thoughtcrime.securesms.database.Address", lpparam.classLoader);

        /*findAndHookConstructor(AddressClass, String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Log.d("crackSignal","address: "+param.args[0]);
            }
        });*/

        findAndHookConstructor(AddressClass, Parcel.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Log.d("crackSignal", "in: " + param.args[0]);
                final Class<?> parcelClass = param.args[0].getClass();
                //Field[] parcelFields=param.args[0].getClass().getDeclaredFields();
                Field[] parcelFields = parcelClass.getDeclaredFields();
                for (Field parcelField : parcelFields) {
                    parcelField.setAccessible(true);
                    Log.d("crackSignal", " \nField Name: " + parcelField.getName() + " \nField Value: " + parcelField.get(param.args[0]));
                }

                Field sHolderPoolinParcelField = findField(parcelClass, "sHolderPool");
                Field sOwnedPoolinParcelField = findField(parcelClass, "sOwnedPool");

                sHolderPoolinParcelField.setAccessible(true);
                sOwnedPoolinParcelField.setAccessible(true);

                //sHolderPoolObject是一个数组
                Object sHolderPoolObject = sHolderPoolinParcelField.get(param.args[0]);
                Object sOwnedPoolObject = sOwnedPoolinParcelField.get(param.args[0]);

                Field[] sHolderPoolFields = sHolderPoolObject.getClass().getDeclaredFields();
                Field[] sOwnedPoolFields = sOwnedPoolObject.getClass().getDeclaredFields();

                for (Field sHolderPoolField : sHolderPoolFields) {
                    sHolderPoolField.setAccessible(true);
                    Log.d("crackSignal", " \nField Name: " + sHolderPoolField.getName() + " \nField Value: " + sHolderPoolField.get(sHolderPoolObject));
                }

                for (Field sOwnedPoolField : sOwnedPoolFields) {
                    sOwnedPoolField.setAccessible(true);
                    Log.d("crackSignal", " \nField Name: " + sOwnedPoolField.getName() + " \nField Value: " + sOwnedPoolField.get(sOwnedPoolObject));
                }
            }
        });
    }

    //遍历作为参数的类实例传入函数的类实例的字段值
    public void hookContext() {
        final Class<?> MessageSenderClass = findClass("org.thoughtcrime.securesms.sms.MessageSender", lpparam.classLoader);
        final Class<?> RecipientClass = findClass("org.thoughtcrime.securesms.recipients.Recipient", lpparam.classLoader);

        findAndHookMethod(MessageSenderClass, "sendTextMessage", Context.class, RecipientClass, boolean.class, boolean.class, long.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                final Class<?> contextClass = param.args[0].getClass();
                Field[] contextFields = contextClass.getDeclaredFields();
                for (Field contextField : contextFields) {
                    contextField.setAccessible(true);
                    Log.d("crackSignal", " \nField Name: " + contextField.getName() + " \nField Value: " + contextField.get(param.args[0]));
                }

                //ExpiringMessageManager
                final Class<?> ExpiringMessageManagerClass = findClass("org.thoughtcrime.securesms.service.ExpiringMessageManager", lpparam.classLoader);
                Field ExpiringMessageManagerinContext = findField(contextClass, "expiringMessageManager");
                Object ExpiringMessageManagerObject = ExpiringMessageManagerinContext.get(param.args[0]);

                Field[] ExpiringMessageManagerFields = ExpiringMessageManagerClass.getDeclaredFields();
                Log.d("crackSignal", "ExpiringMessageManager");
                for (Field ExpiringMessageManagerField : ExpiringMessageManagerFields) {
                    ExpiringMessageManagerField.setAccessible(true);
                    Log.d("crackSignal", "Field Name: " + ExpiringMessageManagerField.getName() + " ;Field Value: " + ExpiringMessageManagerField.get(ExpiringMessageManagerObject));
                }

                //IncomingMessageObserver
                final Class<?> IncomingMessageObserverClass = findClass("org.thoughtcrime.securesms.service.IncomingMessageObserver", lpparam.classLoader);
                Field IncomingMessageObserverinContext = findField(contextClass, "incomingMessageObserver");
                Object IncomingMessageObserverObject = IncomingMessageObserverinContext.get(param.args[0]);

                Field[] IncomingMessageObserverFields = IncomingMessageObserverClass.getDeclaredFields();
                Log.d("crackSignal", "IncomingMessageObserver");
                for (Field IncomingMessageObserverField : IncomingMessageObserverFields) {
                    IncomingMessageObserverField.setAccessible(true);
                    Log.d("crackSignal", "Field Name: " + IncomingMessageObserverField.getName() + " ;Field Value: " + IncomingMessageObserverField.get(IncomingMessageObserverObject));
                }

                //JobManager
                final Class<?> JobManagerClass = findClass("org.thoughtcrime.securesms.jobmanager.JobManager", lpparam.classLoader);
                Field JobManagerinContext = findField(contextClass, "jobManager");
                Object JobManagerObject = JobManagerinContext.get(param.args[0]);

                Field[] JobManagerFields = JobManagerClass.getDeclaredFields();
                Log.d("crackSignal", "JobManager");
                for (Field JobManagerField : JobManagerFields) {
                    JobManagerField.setAccessible(true);
                    Log.d("crackSignal", "Field Name: " + JobManagerField.getName() + " ;Field Value: " + JobManagerField.get(JobManagerObject));
                }

                //PersistentLogger
                final Class<?> PersistentLoggerClass = findClass("org.thoughtcrime.securesms.logging.PersistentLogger", lpparam.classLoader);
                Field PersistentLoggerinContext = findField(contextClass, "persistentLogger");
                Object PersistentLoggerObject = PersistentLoggerinContext.get(param.args[0]);

                Field[] PersistentLoggerFields = PersistentLoggerClass.getDeclaredFields();
                Log.d("crackSignal", "PersistentLogger");
                for (Field PersistentLoggerField : PersistentLoggerFields) {
                    PersistentLoggerField.setAccessible(true);
                    Log.d("crackSignal", "Field Name: " + PersistentLoggerField.getName() + " ;Field Value: " + PersistentLoggerField.get(PersistentLoggerObject));
                }
                Field sercretField = findField(PersistentLoggerClass, "secret");
                Object sercretObject = sercretField.get(PersistentLoggerObject);
                Field[] byteFields = byte[].class.getDeclaredFields();
                for (Field byteField : byteFields) {
                    Log.d("crackSignal", "byte name: " + byteField.get(sercretObject));
                }
            }
        });
    }

    //解包
    public void decrypt() {
        final Class<?> SignalServiceCipherClass = findClass("org.whispersystems.signalservice.api.crypto.SignalServiceCipher", lpparam.classLoader);
        final Class<?> MetadataClass = findClass("org.whispersystems.signalservice.api.crypto.SignalServiceCipher$Metadata", lpparam.classLoader);
        final Class<?> SignalServiceEnvelopeClass = findClass("org.whispersystems.signalservice.api.messages.SignalServiceEnvelope", lpparam.classLoader);

        //String: The Other Side's PhoneNumber 其实没啥用
        findAndHookConstructor(MetadataClass, String.class, int.class, long.class, boolean.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Log.d("crackSignal", "String: " + param.args[0] + " int: " + param.args[0] + " long: " + param.args[0] + " boolean: " + param.args[0]);
            }
        });

        findAndHookMethod(SignalServiceCipherClass, "decrypt", SignalServiceEnvelopeClass, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Log.d("crackSignal", "康一下Envelope里面有什么");
                Field envelopeField = findField(SignalServiceEnvelopeClass, "envelope");
                Object envelopeObject = envelopeField.get(param.args[0]);

                final Class<?> envelopeClass = findClass("org.whispersystems.signalservice.internal.push.SignalServiceProtos$Envelope", lpparam.classLoader);
                Field[] fieldsInEnvelopeClass = envelopeClass.getDeclaredFields();
                for (Field fieldInEnvelopeClass : fieldsInEnvelopeClass) {
                    fieldInEnvelopeClass.setAccessible(true);
                    Log.d("crackSignal", "Field Name: " + fieldInEnvelopeClass.getName() + " Field Value: " + fieldInEnvelopeClass.get(envelopeObject));
                }
            }
        });
    }

    //sender
    public void sender() {
        final Class<?> SignalServiceContentClass = findClass("org.whispersystems.signalservice.api.messages.SignalServiceContent", lpparam.classLoader);
        final Class<?> SignalServiceDataMessageClass = findClass("org.whispersystems.signalservice.api.messages.SignalServiceDataMessage", lpparam.classLoader);

        findAndHookConstructor(SignalServiceContentClass, SignalServiceDataMessageClass, String.class, int.class, long.class, boolean.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Log.d("crackSignal", "String: " + param.args[0]);
            }
        });
    }
}
