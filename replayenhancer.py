from tkinter import *
from tkinter import ttk

def new_configuration():
    ui_title_screen.grid_remove()
    notebook_parameters.grid()

def edit_configuration():
    ui_title_screen.grid_remove()

ui_root = Tk()
ui_root.title("Project CARS Replay Enhancer")

temp_label = StringVar()

ui_title_screen = ttk.Frame(ui_root, padding=(3, 3, 12, 12))
ui_new_configuration = ttk.Button(ui_title_screen, text="New Configuration", command=new_configuration)
ui_edit_configuration = ttk.Button(ui_title_screen, text="Edit Configuration", command=edit_configuration)
ui_temp_label = ttk.Label(ui_title_screen, textvariable=temp_label)

ui_title_screen.grid()
ui_new_configuration.grid(column=1, row=1)
ui_edit_configuration.grid(column=1, row=2)
ui_temp_label.grid(column=0, row=3, columnspan=3)

notebook_parameters = ttk.Notebook(ui_root)

ui_file = ttk.Frame(notebook_parameters, padding=(3, 3, 12, 12))

ui_text = ttk.Frame(notebook_parameters, padding=(3, 3, 12, 12))
notebook_parameters.add(ui_file, text="Files")
notebook_parameters.add(ui_text, text="Text")

ui_root.mainloop()
