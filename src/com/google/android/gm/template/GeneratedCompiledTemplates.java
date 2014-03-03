package com.google.android.gm.template;

import java.io.IOException;
import java.util.Map;

public class GeneratedCompiledTemplates {
	public static final void render_action_strip(Appendable paramAppendable, EvalContext paramEvalContext) {
		try {
			paramAppendable.append("\n<table cellpadding=\"0\" cellspacing=\"0\" class=\"gm-action-strip");
			if (Expression.isTruthy(Variable.evaluate(paramEvalContext,
					"useReplyDefault")))
				;
			for (String str = " gm-action-strip-show-reply";; str = "") {
				Print.printEscaped(paramAppendable, str);
				paramAppendable
						.append("\"><tr>\n<td class=\"gm-star-icon gm-action-button gm-action-button-first\"></td>\n<td class=\"gm-reply-action gm-action-button\">\n<div class=\"gm-action-icon\"></div>\n<div class=\"gm-action-text\">");
				Print.printEscaped(paramAppendable,
						Variable.evaluate(paramEvalContext, "replyString"));
				paramAppendable
						.append("</div>\n</td>\n<td class=\"gm-replyall-action gm-action-button\">\n<div class=\"gm-action-icon\"></div>\n<div class=\"gm-action-text\">");
				Print.printEscaped(paramAppendable,
						Variable.evaluate(paramEvalContext, "replyAllString"));
				paramAppendable
						.append("</div>\n</td>\n<td class=\"gm-forward-action gm-action-button\">\n<div class=\"gm-action-icon\"></div>\n<div class=\"gm-action-text\">");
				Print.printEscaped(paramAppendable,
						Variable.evaluate(paramEvalContext, "forwardString"));
				paramAppendable
						.append("</div>\n</td>\n<td class=\"gm-strip-more gm-action-button\"></td>\n<td class=\"gm-draft-action gm-action-button\">\n<div class=\"gm-action-icon\"></div>\n<div class=\"gm-action-text\">");
				Print.printEscaped(paramAppendable, Variable.evaluate(
						paramEvalContext, "resumeDraftString"));
				paramAppendable.append("</div>\n</td>\n</tr></table>\n");
				return;
			}
		} catch (IOException localIOException) {
			throw new RuntimeException("Exception during template rendering",
					localIOException);
		}
	}

	public static final void render_action_strip(Appendable paramAppendable,
			Map<String, ? extends Object> paramMap) {
		render_action_strip(paramAppendable, new EvalContext(paramMap));
	}

	public static final void render_conversation(final Appendable paramAppendable,
			EvalContext paramEvalContext) {
		try {
			paramAppendable
					.append("\n<html>\n<head>\n<meta name=\"viewport\" content=\"target-densitydpi = device-dpi\"/>\n<meta name=\"viewport\" content=\"user-scalable = false\"/>\n<link rel=\"stylesheet\" href=\"file:///android_res/raw/styles.css\">\n</head>\n<body>\n<div class=\"gm-conversation-header\">\n<div id=\"gm_subject\" style=\"zoom:");
			Print.printEscaped(paramAppendable,
					Variable.evaluate(paramEvalContext, "zoom"));
			paramAppendable.append("\">");
			Print.printEscaped(paramAppendable,
					Variable.evaluate(paramEvalContext, "subject"));
			paramAppendable.append("</div>\n<div id=\"gm_labels\">\n");
			if (Expression.isTruthy(Variable.evaluate(paramEvalContext,
					"isImportant")))
				paramAppendable
						.append("\n<img class=\"gm-important\" src=\"file:///android_res/drawable/ic_email_caret_none_important_unread.png\">\n");
			paramAppendable.append("\n");
			render_labels(paramAppendable, paramEvalContext);
			paramAppendable.append("\n</div>\n</div>\n");
			ForEach.ItemVisitor local5 = new ForEach.ItemVisitor() {
				@SuppressWarnings({ "unchecked", "unchecked" })
				public void visit(EvalContext paramAnonymousEvalContext)
						throws IOException {
					paramAppendable.append("\n");
					if (Expression.isTruthy(Reference.evaluate(
							paramAnonymousEvalContext, "message",
							"isSuperCollapsed"))) {
						paramAppendable.append("\n");
						GeneratedCompiledTemplates.render_super_collapsed(
								paramAppendable,
								new EvalContext((Map) Variable.evaluate(
										paramAnonymousEvalContext, "message")));
						paramAppendable.append("\n");
					}else {
						paramAppendable.append("\n");
						GeneratedCompiledTemplates.render_message(
								paramAppendable,
								new EvalContext((Map) Variable.evaluate(
										paramAnonymousEvalContext, "message")));
						paramAppendable.append("\n");
					}
				}
			};
			ForEach.iterateListExpression(
					Variable.evaluate(paramEvalContext, "messages"), "message",
					paramEvalContext, local5);
			paramAppendable
					.append("\n<div id=\"gm_fixed_header\" style=\"visibility:hidden\">\n<div class=\"gm-header\">\n<div class=\"gm-top-header\">\n<img class=\"gm-contact\" src=\"file:///android_res/drawable/ic_contact_picture.png\">\n<div class=\"gm-draft-icon\"></div>\n");
			render_action_strip(paramAppendable, paramEvalContext);
			paramAppendable
					.append("\n<div class=\"gm-header-icons\">\n<div class=\"gm-presence-icon\" name=\"");
			Print.printEscaped(paramAppendable,
					Variable.evaluate(paramEvalContext, "email"));
			paramAppendable
					.append("_presence\"></div>\n<div class=\"gm-attachment-icon\"></div>\n</div>\n<div class=\"gm-sender-name\"></div>\n<div class=\"gm-sender-email\"></div>\n<div class=\"gm-separator\"></div>\n</div>\n</div>\n</div>\n</body>\n<script type=\"text/javascript\" src=\"file:///android_res/raw/script.js\"></script>\n<script type=\"text/javascript\">\nvar MSG_LOADING = '");
			Print.printEscaped(paramAppendable,
					Variable.evaluate(paramEvalContext, "loadingString"));
			paramAppendable.append("';\nvar MSG_HIDE_ELIDED = '");
			Print.printEscaped(paramAppendable,
					Variable.evaluate(paramEvalContext, "hideElidedString"));
			paramAppendable.append("';\nvar MSG_SHOW_ELIDED = '");
			Print.printEscaped(paramAppendable,
					Variable.evaluate(paramEvalContext, "showElidedString"));
			paramAppendable.append("';\nvar ACCOUNT_URI = '");
			Print.printEscaped(paramAppendable,
					Variable.evaluate(paramEvalContext, "baseUri"));
			paramAppendable.append("';\nvar USE_SNAP_HEADER = ");
			Print.printEscaped(paramAppendable,
					Variable.evaluate(paramEvalContext, "useSnapHeader"));
			paramAppendable.append(";\ngm.init();\n</script>\n</html>\n");
			return;
		} catch (IOException localIOException) {
			throw new RuntimeException("Exception during template rendering",
					localIOException);
		}
	}

	public static final void render_conversation(Appendable paramAppendable,
			Map<String, ? extends Object> paramMap) {
		render_conversation(paramAppendable, new EvalContext(paramMap));
	}

	public static final void render_expanded_body(final Appendable paramAppendable,
			EvalContext paramEvalContext) {
		try {
			paramAppendable.append("\n");
			if (Expression.isTruthy(Variable.evaluate(paramEvalContext,
					"showPrompt"))) {
				paramAppendable
						.append("\n<div class=\"gm-show-pictures\">\n<table border=\"0\" cellpadding=\"9\" cellspacing=\"0\" width=\"100%\">\n<tr valign=\"middle\">\n<td>");
				Print.printEscaped(paramAppendable,
						Variable.evaluate(paramEvalContext, "prompt"));
				paramAppendable
						.append("</td>\n<td align=\"right\">\n<button class=\"gm-show-pictures-button\" onclick=\"gm.showPictures()\">");
				Print.printEscaped(paramAppendable,
						Variable.evaluate(paramEvalContext, "button"));
				paramAppendable
						.append("</button>\n</td>\n</tr>\n</table>\n</div>\n");
			}
			paramAppendable
					.append("\n<div class=\"gm-message-content\" style=\"zoom:");
			Print.printEscaped(paramAppendable,
					Variable.evaluate(paramEvalContext, "zoom"));
			paramAppendable.append("\">");
			Print.print(paramAppendable,
					Variable.evaluate(paramEvalContext, "messageBody"));
			paramAppendable
					.append("</div>\n<div style=\"clear:both\"></div>\n");
			if (Expression.isTruthy(Variable.evaluate(paramEvalContext,
					"attachments"))) {
				paramAppendable.append("\n");
				ForEach.ItemVisitor local1 = new ForEach.ItemVisitor() {
					public void visit(EvalContext paramAnonymousEvalContext)
							throws IOException {
						paramAppendable
								.append("\n<div class=\"attachment\">\n<table class=\"attachment\" border=\"0\">\n");
						if (Expression.isTruthy(Reference.evaluate(
								paramAnonymousEvalContext, "attachment",
								"displayInline"))) {
							paramAppendable
									.append("\n<tr>\n<td class=\"attachment-icon\"><img style=\"width:");
							Print.printEscaped(paramAppendable, Reference
									.evaluate(paramAnonymousEvalContext,
											"attachment", "width"));
							paramAppendable.append("\" src=\"");
							Print.printEscaped(paramAppendable, Reference
									.evaluate(paramAnonymousEvalContext,
											"attachment", "uri"));
							paramAppendable
									.append("\"></td>\n</tr>\n<tr>\n<td class=\"attachment-name\"><b>");
							Print.printEscaped(paramAppendable, Reference
									.evaluate(paramAnonymousEvalContext,
											"attachment", "name"));
							paramAppendable.append("</b> (");
							Print.printEscaped(paramAppendable, Reference
									.evaluate(paramAnonymousEvalContext,
											"attachment", "size"));
							paramAppendable
									.append(")</td>\n</tr>\n<tr>\n<td class=\"attachment\">\n");
							if (Expression.isTruthy(Reference.evaluate(
									paramAnonymousEvalContext, "attachment",
									"canDownload"))) {
								paramAppendable
										.append("\n<button class=\"attachment\"\nonclick=\"window.gmail.download('");
								Print.print(paramAppendable, Variable.evaluate(
										paramAnonymousEvalContext, "messageId"));
								paramAppendable.append("', ");
								Print.printEscaped(paramAppendable, Reference
										.evaluate(paramAnonymousEvalContext,
												"attachment", "partIndex"));
								paramAppendable.append(")\">");
								Print.printEscaped(paramAppendable, Variable
										.evaluate(paramAnonymousEvalContext,
												"downloadString"));
								paramAppendable.append("</button>\n");
							}
							paramAppendable.append("\n");
							if (Expression.isTruthy(Reference.evaluate(
									paramAnonymousEvalContext, "attachment",
									"canView"))) {
								paramAppendable
										.append("\n<button class=\"attachment\"\nonclick=\"window.gmail.preview('");
								Print.print(paramAppendable, Variable.evaluate(
										paramAnonymousEvalContext, "messageId"));
								paramAppendable.append("', ");
								Print.printEscaped(paramAppendable, Reference
										.evaluate(paramAnonymousEvalContext,
												"attachment", "partIndex"));
								paramAppendable.append(")\">");
								Print.printEscaped(paramAppendable, Variable
										.evaluate(paramAnonymousEvalContext,
												"previewString"));
								paramAppendable.append("</button>\n");
							}
							paramAppendable.append("\n</td>\n</tr>\n");
						}else{
							paramAppendable
									.append("\n<tr valign=\"top\">\n<td class=\"attachmentPreview\"><img class=\"attachmentPreview\" src=\"");
							Print.printEscaped(paramAppendable, Reference
									.evaluate(paramAnonymousEvalContext,
											"attachment", "uri"));
							paramAppendable
									.append("\"></td>\n<td class=\"attachment-name\">");
							Print.printEscaped(paramAppendable, Reference
									.evaluate(paramAnonymousEvalContext,
											"attachment", "name"));
							paramAppendable.append("<br>");
							Print.printEscaped(paramAppendable, Reference
									.evaluate(paramAnonymousEvalContext,
											"attachment", "size"));
							paramAppendable
									.append("</td>\n<td class=\"attachment\">\n");
							if (Expression.isTruthy(Reference.evaluate(
									paramAnonymousEvalContext, "attachment",
									"canInstall"))) {
								paramAppendable
										.append("\n<button class=\"attachment\"\nonclick=\"window.gmail.download('");
								Print.print(paramAppendable, Variable.evaluate(
										paramAnonymousEvalContext, "messageId"));
								paramAppendable.append("', ");
								Print.printEscaped(paramAppendable, Reference
										.evaluate(paramAnonymousEvalContext,
												"attachment", "partIndex"));
								paramAppendable.append(")\">");
								Print.printEscaped(paramAppendable, Variable
										.evaluate(paramAnonymousEvalContext,
												"installString"));
								paramAppendable.append("</button>\n<br>\n");
							}
							paramAppendable.append("\n");
							if (Expression.isTruthy(Reference.evaluate(
									paramAnonymousEvalContext, "attachment",
									"canDownload"))) {
								paramAppendable
										.append("\n<button class=\"attachment\"\nonclick=\"window.gmail.download('");
								Print.print(paramAppendable, Variable.evaluate(
										paramAnonymousEvalContext, "messageId"));
								paramAppendable.append("', ");
								Print.printEscaped(paramAppendable, Reference
										.evaluate(paramAnonymousEvalContext,
												"attachment", "partIndex"));
								paramAppendable.append(")\">");
								Print.printEscaped(paramAppendable, Variable
										.evaluate(paramAnonymousEvalContext,
												"downloadString"));
								paramAppendable.append("</button>\n<br>\n");
							}
							paramAppendable.append("\n");
							if (Expression.isTruthy(Reference.evaluate(
									paramAnonymousEvalContext, "attachment",
									"canView"))) {
								paramAppendable
										.append("\n<button class=\"attachment\"\nonclick=\"window.gmail.preview('");
								Print.print(paramAppendable, Variable.evaluate(
										paramAnonymousEvalContext, "messageId"));
								paramAppendable.append("', ");
								Print.printEscaped(paramAppendable, Reference
										.evaluate(paramAnonymousEvalContext,
												"attachment", "partIndex"));
								paramAppendable.append(")\">");
								Print.printEscaped(paramAppendable, Variable
										.evaluate(paramAnonymousEvalContext,
												"previewString"));
								paramAppendable.append("</button>\n<br>\n");
							}
							paramAppendable.append("\n</td>\n</tr>\n");
						}
					}
				};
				ForEach.iterateListExpression(
						Variable.evaluate(paramEvalContext, "attachments"),
						"attachment", paramEvalContext, local1);
				paramAppendable.append("\n");
			}
			paramAppendable.append("\n");
			return;
		} catch (IOException localIOException) {
			throw new RuntimeException("Exception during template rendering",
					localIOException);
		}
	}

	public static final void render_expanded_body(Appendable paramAppendable,
			Map<String, ? extends Object> paramMap) {
		render_expanded_body(paramAppendable, new EvalContext(paramMap));
	}

	public static final void render_labels(final Appendable paramAppendable,
			EvalContext paramEvalContext) {
		try {
			paramAppendable.append("\n");
			ForEach.ItemVisitor local2 = new ForEach.ItemVisitor() {
				public void visit(EvalContext paramAnonymousEvalContext)
						throws IOException {
					paramAppendable.append("\n<div class=\"gm-label\"\nstyle=\"background:");
					Print.printEscaped(paramAppendable, Reference.evaluate(
							paramAnonymousEvalContext, "label", "background"));
					paramAppendable.append(";border-color:");
					Print.printEscaped(paramAppendable, Reference.evaluate(
							paramAnonymousEvalContext, "label", "borderColor"));
					paramAppendable.append(";color:");
					Print.printEscaped(paramAppendable, Reference.evaluate(
							paramAnonymousEvalContext, "label", "color"));
					paramAppendable.append("\">\n");
					Print.printEscaped(paramAppendable, Reference.evaluate(
							paramAnonymousEvalContext, "label", "name"));
					paramAppendable.append("\n</div>\n");
				}
			};
			ForEach.iterateListExpression(
					Variable.evaluate(paramEvalContext, "labels"), "label",
					paramEvalContext, local2);
			paramAppendable.append("\n");
			return;
		} catch (IOException localIOException) {
			throw new RuntimeException("Exception during template rendering",
					localIOException);
		}
	}

	public static final void render_labels(Appendable paramAppendable,
			Map<String, ? extends Object> paramMap) {
		render_labels(paramAppendable, new EvalContext(paramMap));
	}

	public static final void render_loading_body(Appendable paramAppendable,
			EvalContext paramEvalContext) {
		try {
			paramAppendable
					.append("\n<html>\n<head>\n<meta name=\"viewport\" content=\"target-densitydpi = device-dpi\"/>\n<meta name=\"viewport\" content=\"user-scalable = false\"/>\n<link rel=\"stylesheet\" href=\"file:///android_res/raw/styles.css\">\n</head>\n<body>\n<div class=\"gm-loading\" style=\"zoom:");
			Print.printEscaped(paramAppendable,
					Variable.evaluate(paramEvalContext, "zoom"));
			paramAppendable.append("; font-weight:bold\">");
			Print.print(paramAppendable,
					Variable.evaluate(paramEvalContext, "loading"));
			paramAppendable.append("</div>\n</body>\n</html>\n");
			return;
		} catch (IOException localIOException) {
			throw new RuntimeException("Exception during template rendering",
					localIOException);
		}
	}

	public static final void render_loading_body(Appendable paramAppendable,
			Map<String, ? extends Object> paramMap) {
		render_loading_body(paramAppendable, new EvalContext(paramMap));
	}

	public static final void render_message(Appendable paramAppendable,
			EvalContext paramEvalContext) throws IOException {
		while (true) {
			try {
				paramAppendable.append("\n<div id=\"");
				Print.print(paramAppendable,
						Variable.evaluate(paramEvalContext, "messageId"));
				paramAppendable.append("\" index=\"");
				Print.print(paramAppendable,
						Variable.evaluate(paramEvalContext, "index"));
				paramAppendable.append("\" class=\"");
				Print.print(paramAppendable,
						Variable.evaluate(paramEvalContext, "classNames"));
				paramAppendable
						.append("\">\n<div class=\"gm-header\">\n<div class=\"gm-top-header\">\n<img class=\"gm-contact\" name=\"");
				Print.printEscaped(paramAppendable,
						Variable.evaluate(paramEvalContext, "email"));
				paramAppendable
						.append("_photo\"\nsrc=\"file:///android_res/drawable/ic_contact_picture.png\">\n<div class=\"gm-draft-icon\"></div>\n");
				render_action_strip(paramAppendable, paramEvalContext);
				paramAppendable
						.append("\n<div class=\"gm-date-time\">\n<div class=\"gm-date\">");
				Print.printEscaped(paramAppendable,
						Variable.evaluate(paramEvalContext, "date"));
				paramAppendable.append("</div>\n<div class=\"gm-time\">");
				Print.printEscaped(paramAppendable,
						Variable.evaluate(paramEvalContext, "time"));
				paramAppendable
						.append("</div>\n</div>\n<div class=\"gm-header-icons\">\n<div class=\"gm-presence-icon\" name=\"");
				Print.printEscaped(paramAppendable,
						Variable.evaluate(paramEvalContext, "email"));
				paramAppendable
						.append("_presence\"></div>\n<div class=\"gm-attachment-icon\"></div>\n</div>\n<div class=\"gm-sender-name\" style=\"color:");
				Print.printEscaped(paramAppendable,
						Variable.evaluate(paramEvalContext, "color"));
				paramAppendable.append("\">");
				if (Expression.isTruthy(Variable.evaluate(paramEvalContext,
						"personal"))) {
					Print.printEscaped(paramAppendable,
							Variable.evaluate(paramEvalContext, "personal"));
					paramAppendable
							.append("</div>\n<div class=\"gm-sender-email\">");
					if (Expression.isTruthy(Variable.evaluate(paramEvalContext,
							"personal")))
						Print.print(paramAppendable, Variable.evaluate(
								paramEvalContext, "clickSafeEmail"));
					paramAppendable
							.append("</div>\n<div class=\"gm-separator\"></div>\n<div class=\"gm-snippet\" style=\"zoom:");
					Print.printEscaped(paramAppendable,
							Variable.evaluate(paramEvalContext, "zoom"));
					paramAppendable.append("\">");
					Print.printEscaped(paramAppendable,
							Variable.evaluate(paramEvalContext, "snippet"));
					paramAppendable
							.append("</div>\n</div>\n<div class=\"gm-bottom-header\">\n<div class=\"gm-date-time\">\n<div class=\"gm-date\">");
					Print.printEscaped(paramAppendable,
							Variable.evaluate(paramEvalContext, "date"));
					paramAppendable.append("</div>\n<div class=\"gm-time\">");
					Print.printEscaped(paramAppendable,
							Variable.evaluate(paramEvalContext, "time"));
					paramAppendable.append("</div>\n</div>\n<div class=\"");
					if (Expression.isTruthy(Variable.evaluate(paramEvalContext,
							"recipientsCollapsible"))) {
						paramAppendable.append("gm-recipients-collapsible");
						paramAppendable
								.append("\">\n<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n");
						if (Expression.isTruthy(Variable.evaluate(
								paramEvalContext, "toAddresses"))) {
							paramAppendable
									.append("\n<tr valign=\"top\">\n<td class=\"gm-recipient-title\">");
							Print.printEscaped(paramAppendable, Variable
									.evaluate(paramEvalContext, "toString"));
							paramAppendable
									.append("</td>\n<td class=\"gm-recipient-list\">");
							Print.print(paramAppendable, Variable.evaluate(
									paramEvalContext, "toAddresses"));
							paramAppendable.append("</td>\n</tr>\n");
						}
						paramAppendable.append("\n");
						if (Expression.isTruthy(Variable.evaluate(
								paramEvalContext, "ccAddresses"))) {
							paramAppendable
									.append("\n<tr valign=\"top\">\n<td class=\"gm-recipient-title\">");
							Print.printEscaped(paramAppendable, Variable
									.evaluate(paramEvalContext, "ccString"));
							paramAppendable
									.append("</td>\n<td class=\"gm-recipient-list\">");
							Print.print(paramAppendable, Variable.evaluate(
									paramEvalContext, "ccAddresses"));
							paramAppendable.append("</td>\n</tr>\n");
						}
						paramAppendable.append("\n");
						if (Expression.isTruthy(Variable.evaluate(
								paramEvalContext, "recipientsCollapsible"))) {
							paramAppendable
									.append("\n<tr valign=\"top\">\n<td class=\"gm-recipient-title\"></td>\n<td class=\"gm-recipient-list gm-details\">");
							Print.printEscaped(paramAppendable, Variable
									.evaluate(paramEvalContext,
											"showDetailsString"));
							paramAppendable.append("</td>\n</tr>\n");
						}
						paramAppendable
								.append("\n</table>\n</div>\n<div style=\"clear:both\"></div>\n</div>\n</div>\n<div class=\"gm-body\">");
						if (Expression.isTruthy(Variable.evaluate(
								paramEvalContext, "isExpanded")))
							render_expanded_body(paramAppendable,
									paramEvalContext);
						paramAppendable.append("</div>\n</div>\n");
					}
				} else {
					Print.print(paramAppendable, Variable.evaluate(
							paramEvalContext, "clickSafeEmail"));
					continue;
				}
			} catch (IOException localIOException) {
				throw new RuntimeException(
						"Exception during template rendering", localIOException);
			}
			paramAppendable.append("gm-recipients");
		}
	}

	public static final void render_message(Appendable paramAppendable,
			Map<String, Object> paramMap) {
		try {
			render_message(paramAppendable, new EvalContext(paramMap));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static final void render_recipient_details(
			final Appendable paramAppendable, EvalContext paramEvalContext) {
		try {
			paramAppendable
					.append("\n<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n<tr valign=\"top\">\n<td class=\"gm-recipient-title\">");
			Print.printEscaped(paramAppendable,
					Variable.evaluate(paramEvalContext, "dateString"));
			paramAppendable
					.append("</td>\n<td class=\"gm-recipient-list\"><div class=\"gm-recipient\">");
			Print.printEscaped(paramAppendable,
					Variable.evaluate(paramEvalContext, "date"));
			paramAppendable.append(" ");
			Print.printEscaped(paramAppendable,
					Variable.evaluate(paramEvalContext, "time"));
			paramAppendable.append("</div></td>\n</tr>\n");
			if (Expression.isTruthy(Variable.evaluate(paramEvalContext,
					"toAddresses"))) {
				paramAppendable
						.append("\n<tr valign=\"top\">\n<td class=\"gm-recipient-title\">");
				Print.printEscaped(paramAppendable,
						Variable.evaluate(paramEvalContext, "toString"));
				paramAppendable
						.append("</td>\n<td class=\"gm-recipient-list\">\n");
				ForEach.ItemVisitor local3 = new ForEach.ItemVisitor() {
					public void visit(EvalContext paramAnonymousEvalContext)
							throws IOException {
						paramAppendable
								.append("\n<div class=\"gm-recipient\">\n");
						if (Expression.isTruthy(Reference.evaluate(paramAnonymousEvalContext, "addr", "name"))){
							Print.printEscaped(paramAppendable, Reference
									.evaluate(paramAnonymousEvalContext,
											"addr", "name"));
							paramAppendable.append("\n");
							if (Expression.isTruthy(Reference.evaluate(
									paramAnonymousEvalContext, "addr", "name"))) {
								paramAppendable
										.append("&lt;<a class=\"gm-email\" href=\"mailto:");
								Print.print(paramAppendable, Reference.evaluate(
										paramAnonymousEvalContext, "addr",
										"email"));
								paramAppendable.append("\">");
								Print.print(paramAppendable, Reference.evaluate(
										paramAnonymousEvalContext, "addr",
										"email"));
								paramAppendable.append("</a>&gt;");
							}
							paramAppendable.append("\n</div>\n");
						}
						else{
							paramAppendable
									.append("<a class=\"gm-email\" href=\"mailto:");
							Print.print(paramAppendable, Reference.evaluate(
									paramAnonymousEvalContext, "addr", "email"));
							paramAppendable.append("\">");
							Print.print(paramAppendable, Reference.evaluate(
									paramAnonymousEvalContext, "addr", "email"));
							paramAppendable.append("</a>");
						}
					}
				};
				ForEach.iterateListExpression(
						Variable.evaluate(paramEvalContext, "toAddresses"),
						"addr", paramEvalContext, local3);
				paramAppendable.append("\n</td>\n</tr>\n");
			}
			paramAppendable.append("\n");
			if (Expression.isTruthy(Variable.evaluate(paramEvalContext,
					"ccAddresses"))) {
				paramAppendable
						.append("\n<tr valign=\"top\">\n<td class=\"gm-recipient-title\">");
				Print.printEscaped(paramAppendable,
						Variable.evaluate(paramEvalContext, "ccString"));
				paramAppendable
						.append("</td>\n<td class=\"gm-recipient-list\">\n");
				ForEach.ItemVisitor local4 = new ForEach.ItemVisitor() {
					public void visit(EvalContext paramAnonymousEvalContext)
							throws IOException {
						paramAppendable
								.append("\n<div class=\"gm-recipient\">\n");
						if (Expression.isTruthy(Reference.evaluate(
								paramAnonymousEvalContext, "addr", "name"))){
							Print.printEscaped(paramAppendable, Reference
									.evaluate(paramAnonymousEvalContext,
											"addr", "name"));
							paramAppendable.append("\n");
							if (Expression.isTruthy(Reference.evaluate(
									paramAnonymousEvalContext, "addr", "name"))) {
								paramAppendable
										.append("&lt;<a class=\"gm-email\" href=\"mailto:");
								Print.print(paramAppendable, Reference.evaluate(
										paramAnonymousEvalContext, "addr",
										"email"));
								paramAppendable.append("\">");
								Print.print(paramAppendable, Reference.evaluate(
										paramAnonymousEvalContext, "addr",
										"email"));
								paramAppendable.append("</a>&gt;");
							}
							paramAppendable.append("\n</div>\n");
						}else{
							paramAppendable
									.append("<a class=\"gm-email\" href=\"mailto:");
							Print.print(paramAppendable, Reference.evaluate(
									paramAnonymousEvalContext, "addr", "email"));
							paramAppendable.append("\">");
							Print.print(paramAppendable, Reference.evaluate(
									paramAnonymousEvalContext, "addr", "email"));
							paramAppendable.append("</a>");
						}
					}
				};
				ForEach.iterateListExpression(
						Variable.evaluate(paramEvalContext, "ccAddresses"),
						"addr", paramEvalContext, local4);
				paramAppendable.append("\n</td>\n</tr>\n");
			}
			paramAppendable
					.append("\n<tr valign=\"top\">\n<td class=\"gm-recipient-title\"></td>\n<td class=\"gm-recipient-list gm-details\">");
			Print.printEscaped(paramAppendable,
					Variable.evaluate(paramEvalContext, "hideDetailsString"));
			paramAppendable.append("</td>\n</tr>\n</table>\n");
			return;
		} catch (IOException localIOException) {
			throw new RuntimeException("Exception during template rendering",
					localIOException);
		}
	}

	public static final void render_recipient_details(
			Appendable paramAppendable, Map<String, ? extends Object> paramMap) {
		render_recipient_details(paramAppendable, new EvalContext(paramMap));
	}

	public static final void render_super_collapsed(Appendable paramAppendable,
			EvalContext paramEvalContext) throws IOException {
		while (true) {
			try {
				paramAppendable
						.append("\n<!-- Template for a block of super-collapsed headers. -->\n<div style=\"position:relative\">\n<div class=\"gm-super-collapsed-block ");
				if (Expression.isTruthy(Boolean.valueOf(Comparison.isEqual(
						Variable.evaluate(paramEvalContext, "count"),
						new Integer(1))))) {
					paramAppendable.append("size1");
					paramAppendable
							.append("\">\n<div class=\"gm-super-collapsed-label\">");
					Print.printEscaped(paramAppendable,
							Variable.evaluate(paramEvalContext, "messagesRead"));
					paramAppendable
							.append("</div>\n</div>\n<!-- Invisible click overlay so that the click area may be bigger than the exposed\nrendered area. -->\n<div class=\"gm-super-collapsed-clicker\" onclick=\"gm.uncollapse(this, ");
					Print.printEscaped(paramAppendable,
							Variable.evaluate(paramEvalContext, "firstIndex"));
					paramAppendable.append(", ");
					Print.printEscaped(paramAppendable,
							Variable.evaluate(paramEvalContext, "lastIndex"));
					paramAppendable.append(")\"> </div>\n</div>\n");
					return;
				}
				if (Expression.isTruthy(Boolean.valueOf(Comparison.isEqual(
						Variable.evaluate(paramEvalContext, "count"),
						new Integer(2))))) {
					paramAppendable.append("size2");
					continue;
				}
			} catch (IOException localIOException) {
				throw new RuntimeException(
						"Exception during template rendering", localIOException);
			}
			paramAppendable.append("sizen");
		}
	}

	public static final void render_super_collapsed(Appendable paramAppendable,
			Map<String,Object> paramMap) {
		try {
			render_super_collapsed(paramAppendable, new EvalContext(paramMap));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

/*
 * Location: C:\Users\科\Desktop\classes_dex2jar.jar Qualified Name:
 * com.google.android.gm.template.GeneratedCompiledTemplates JD-Core Version:
 * 0.6.2
 */