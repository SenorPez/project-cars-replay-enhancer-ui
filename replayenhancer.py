from tkinter import *
from tkinter import ttk

import os.path

def new_configuration():
    ui_title_screen.grid_remove()
    notebook_parameters.grid()

def edit_configuration():
    ui_title_screen.grid_remove()

def __choose_source_file():
    selected_file = filedialog.askopenfilename()

    if selected_file:
        source_file.set(os.path.sep+os.path.relpath(selected_file))
    
def __clear_source_file():
    source_file.set('')

def __choose_source_tele():
    selected_tele = filedialog.askdirectory(mustexist=True)

    if selected_tele:
        source_tele.set(os.path.sep+os.path.relpath(selected_tele))

def __clear_source_tele():
    source_tele.set('')

def __choose_output_file():
    selected_output = filedialog.asksaveasfilename(
        defaultextension = ".mp4",
        filetypes = (("MP4", "*.mp4"),
                     ("AVI", "*.avi"),
                     ("MOV", "*.mov"),
                     ("WEBM", "*.webm"),
                     ("GIF", "*.gif"),
                     ("All files", "*.*")))

    if selected_output:
        output_file.set(os.path.sep+os.path.relpath(selected_output))

def __choose_output_config_file():
    selected_output = filedialog.asksaveasfilename(
        defaultextension = ".json",
        filetype = (("JSON", "*.json"),
                    ("All files", "*.*")))

    if selected_output:
        output_config.set(os.path.sep+os.path.relpath(selected_output))

def __clear_output_file():
    output_file.set('')

def __choose_file(var):
    selected_file = filedialog.askopenfilename()

    if selected_file:
        var.set(os.path.sep+os.path.relpath(selected_file))

def __clear_file(var):
    var.set('')

def __is_int(var):
    try:
        _ = int(var.get())
    except:
        return False
    else:
        return True

def __is_float(var):
    try:
        _ = float(var.get())
    except:
        return False
    else:
        return True

def __sync_help():
    help_window = Toplevel()
    help_window.title("Telemetry Sync Help.")

    help_text = "The Telemetry Sync value is used to synchronize the "+ \
                "playback of the video with the playback of the telemetry "+ \
                "data feed.\n"+ \
                "If the playback is not synchronized, events such as passes "+ \
                "sector completion, and lap times will not be overlayed "+ \
                "correctly.\n"+ \
                "To determine the Telemetry Sync value, create a Low Quality "+ \
                "video, and observe when the car providing the telemetry "+ \
                "data crosses the Start/Finish line to start Lap 2.\n" + \
                "If the Timer created by the Project CARS Replay Enhancer is "+ \
                "at 0.00 and Lap 2, you are properly synced.\n" + \
                "If the Timer has already started Lap 2, the telemetry is "+ \
                "starting too soon. Add the time on the "+ \
                "timer to the Telemetry Sync value.\n" + \
                "If the Timer has not yet started Lap 2, the telemetry is "+ \
                "starting too late. Subtract the difference of the actual Lap 1 "+ \
                "time (shown on the Standings created by the Project CARS Replay "+ \
                "Enhancer) from the time shown on the Timer from the Telemetry "+ \
                "Sync value.\n"+ \
                "If you have any questions, please feel free to contact me "+ \
                "through GitHub for assistance!"
    help_message = Message(help_window, text=help_text)
    help_message.pack()

    close_button = ttk.Button(help_window, text="Close", command=help_window.destroy)
    close_button.pack()

def __add_points(widget):
    try:
        _ = int(widget.get())
    except:
        return False
    else:
        points_structure.append(widget)
        __draw_points_structure()

def __remove_points(position):
    del points_structure[position]
    for widget in grp_points_structure.winfo_children():
        widget.grid_forget()
    __draw_points_structure()

def __draw_points_structure():    
    if use_points_structure.get():
        grp_points_structure.grid(row=3, column=1)

        ttk.Label(grp_points_structure, text="Position").grid(row=1, column=1)
        ttk.Label(grp_points_structure, text="Point Value").grid(row=1, column=2)

        for position, widget in enumerate(points_structure):
            if position == 0:
                ttk.Label(grp_points_structure, text="Fastest Lap Bonus").grid(row=position+2, column=1)
            else:
                ttk.Label(grp_points_structure, text=position).grid(row=position+2, column=1)
                ttk.Button(grp_points_structure, text="Remove Value", command=lambda position=position: __remove_points(position)).grid(row=position+2, column=3)          
            widget.grid(row=position+2, column=2)

        ttk.Label(grp_points_structure, text=position+1).grid(row=position+3, column=1)
        new_entry = ttk.Entry(grp_points_structure)
        new_entry.grid(row=position+3, column=2)
        ttk.Button(grp_points_structure, text="Add Value", command=lambda: __add_points(new_entry)).grid(row=position+3, column=3)
    else:
        grp_points_structure.grid_forget()

ui_root = Tk()
ui_root.title("Project CARS Replay Enhancer")

ui_title_screen = ttk.Frame(ui_root, padding=(3, 3, 12, 12))
ui_new_configuration = ttk.Button(ui_title_screen, text="New Configuration", command=new_configuration)
ui_edit_configuration = ttk.Button(ui_title_screen, text="Edit Configuration", command=edit_configuration)

ui_title_screen.grid()
ui_new_configuration.grid(column=1, row=1)
ui_edit_configuration.grid(column=1, row=2)

notebook_parameters = ttk.Notebook(ui_root)

test = ttk.tclobjs_to_py

ui_files = ttk.Frame(notebook_parameters, padding=(3, 3, 12, 12))

grp_source = ttk.LabelFrame(ui_files, text="Source Files")

source_file = StringVar()
lbl_source_file = ttk.Label(grp_source, text="Source Video File")
disp_source_file = ttk.Entry(grp_source, state="readonly", textvariable=source_file)
choose_source_file = ttk.Button(grp_source, text="Select", command=__choose_source_file)
clear_source_file = ttk.Button(grp_source, text="Clear", command=__clear_source_file)

skip_start = StringVar()
skip_end = StringVar()
sync_racestart = StringVar()

lbl_trimming = ttk.Label(grp_source, text="Seconds to skip in video:")
lbl_skip_start = ttk.Label(grp_source, text="At Start")
disp_skip_start = ttk.Entry(grp_source, textvariable=skip_start, validate='focus', validatecommand=lambda: __is_float(skip_start))
lbl_skip_end = ttk.Label(grp_source, text="At End")
disp_skip_end = ttk.Entry(grp_source, textvariable=skip_end, validate='focus', validatecommand=lambda: __is_float(skip_end))

source_tele = StringVar()
lbl_source_tele = ttk.Label(grp_source, text="Source Telemetry Directory")
disp_source_tele = ttk.Entry(grp_source, state="readonly", textvariable=source_tele)
choose_source_tele = ttk.Button(grp_source, text="Select", command=__choose_source_tele)
clear_source_tele = ttk.Button(grp_source, text="Clear", command=__clear_source_tele)

lbl_sync_racestart = ttk.Label(grp_source, text="Telemetry Sync Value")
disp_sync_racestart = ttk.Entry(grp_source, textvariable=sync_racestart, validate='focus', validatecommand=lambda: __is_float(sync_racestart))
help_sync_racestart = ttk.Button(grp_source, text="More Info", command=__sync_help)

grp_output = ttk.LabelFrame(ui_files, text="Output Files")

output_file = StringVar()
lbl_output_file = ttk.Label(grp_output, text="Output Video File")
disp_output_file = ttk.Entry(grp_output, state="readonly", textvariable=output_file)
choose_output_file = ttk.Button(grp_output, text="Select", command=__choose_output_file)
clear_output_file = ttk.Button(grp_output, text="Clear", command=__clear_output_file)

output_config = StringVar()
lbl_output_config = ttk.Label(grp_output, text="Output Configuration File")
disp_output_config = ttk.Entry(grp_output, state="readonly", textvariable=output_config)
choose_output_config = ttk.Button(grp_output, text="Select", command=__choose_output_config_file)
clear_output_config = ttk.Button(grp_output, text="Clear", command=lambda: __clear_file(output_config))

ui_text = ttk.Frame(notebook_parameters, padding=(3, 3, 12, 12))

heading_font = StringVar()
heading_font_size = StringVar()
heading_text = StringVar()
subheading_text = StringVar()

grp_heading = ttk.LabelFrame(ui_text, text="Heading")
lbl_heading_font = ttk.Label(grp_heading, text="Heading Font")
disp_heading_font = ttk.Entry(grp_heading, state="readonly", textvariable=heading_font)
choose_heading_font = ttk.Button(grp_heading, text="Select", command=lambda: __choose_file(heading_font))
clear_heading_font = ttk.Button(grp_heading, text="Clear", command=lambda: __clear_file(heading_font))

lbl_heading_font_size = ttk.Label(grp_heading, text="Heading Font Size")
disp_heading_font_size = ttk.Entry(grp_heading, textvariable=heading_font_size, validate='focus', validatecommand=lambda: __is_int(heading_font_size))

lbl_heading_text = ttk.Label(grp_heading, text="Heading Text")
disp_heading_text = ttk.Entry(grp_heading, textvariable=heading_text)
lbl_subheading_text = ttk.Label(grp_heading, text="Subheading Text")
disp_subheading_text = ttk.Entry(grp_heading, textvariable=subheading_text)

font = StringVar()
font_size = StringVar()

grp_text = ttk.LabelFrame(ui_text, text="Other Text")
lbl_font = ttk.Label(grp_text, text="Text Font")
disp_font = ttk.Entry(grp_text, state="readonly", textvariable=font)
choose_font = ttk.Button(grp_text, text="Select", command=lambda: __choose_file(font))
clear_font = ttk.Button(grp_text, text="Clear", command=lambda: __clear_file(font))

lbl_font_size = ttk.Label(grp_text, text="Text Font Size")
disp_font_size = ttk.Entry(grp_text, textvariable=font_size, validate='focus', validatecommand=lambda: __is_int(font_size))

ui_graphics = ttk.Frame(notebook_parameters, padding=(3, 3, 12, 12))

backdrop = StringVar()
logo = StringVar()
logo_height = StringVar()
logo_width = StringVar()
series_logo = StringVar()

grp_backdrop = ttk.LabelFrame(ui_graphics, text="Background")
lbl_backdrop = ttk.Label(grp_backdrop, text="Background Image")
disp_backdrop = ttk.Entry(grp_backdrop, state="readonly", textvariable=backdrop)
choose_backdrop = ttk.Button(grp_backdrop, text="Select", command=lambda: __choose_file(backdrop))
clear_backdrop = ttk.Button(grp_backdrop, text="Clear", command=lambda: __clear_file(backdrop))

grp_logo = ttk.LabelFrame(ui_graphics, text="Background Logo")
lbl_logo = ttk.Label(grp_logo, text="Logo Image")
disp_logo = ttk.Entry(grp_logo, state="readonly", textvariable=logo)
choose_logo = ttk.Button(grp_logo, text="Select", command=lambda: __choose_file(logo))
clear_logo = ttk.Button(grp_logo, text="Clear", command=lambda: __clear_file(logo))

lbl_logo_width = ttk.Label(grp_logo, text="Logo Width")
disp_logo_width = ttk.Entry(grp_logo, textvariable=logo_width, validate='focus', validatecommand=lambda: __is_int(logo_width))

lbl_logo_height = ttk.Label(grp_logo, text="Logo Height")
disp_logo_height = ttk.Entry(grp_logo, textvariable=logo_height, validate='focus', validatecommand=lambda: __is_int(logo_height))

grp_series_logo = ttk.LabelFrame(ui_graphics, text="Series Logo")
lbl_series_logo = ttk.Label(grp_series_logo, text="Series Logo")
disp_series_logo = ttk.Entry(grp_series_logo, state="readonly", textvariable=series_logo)
choose_series_logo = ttk.Button(grp_series_logo, text="Select", command=lambda: __choose_file(series_logo))
clear_series_logo = ttk.Button(grp_series_logo, text="Clear", command=lambda: __clear_file(series_logo))

ui_series = ttk.Frame(notebook_parameters, padding=(3, 3, 12, 12))

show_champion = BooleanVar()
use_points_structure = BooleanVar()
bonus_points = IntVar()

disp_show_champion = ttk.Checkbutton(ui_series, text="Show Series Champion?", variable=show_champion)
disp_points_structure = ttk.Checkbutton(ui_series, text="Use Point Structure?", variable=use_points_structure, command=__draw_points_structure)

grp_points_structure = ttk.LabelFrame(ui_series, text="Points Structure")
points_structure = [ttk.Entry(grp_points_structure)]

notebook_parameters.add(ui_files, text="Files")
notebook_parameters.add(ui_text, text="Text Options")
notebook_parameters.add(ui_graphics, text="Graphics Options")
notebook_parameters.add(ui_series, text="Series Options")

grp_source.grid(row=1, column=1)

lbl_source_file.grid(row=1, column=1)
disp_source_file.grid(row=1, column=2)
choose_source_file.grid(row=1, column=3)
clear_source_file.grid(row=1, column=4)

lbl_trimming.grid(row=2, column=1, columnspan=4)
lbl_skip_start.grid(row=3, column=1)
disp_skip_start.grid(row=3, column=2)
lbl_skip_end.grid(row=4, column=1)
disp_skip_end.grid(row=4, column=2)

lbl_source_tele.grid(row=5, column=1)
disp_source_tele.grid(row=5, column=2)
choose_source_tele.grid(row=5, column=3)
clear_source_tele.grid(row=5, column=4)

lbl_sync_racestart.grid(row=6, column=1)
disp_sync_racestart.grid(row=6, column=2)
help_sync_racestart.grid(row=6, column=4)

grp_output.grid(row=2, column=1)

lbl_output_file.grid(row=1, column=1)
disp_output_file.grid(row=1, column=2)
choose_output_file.grid(row=1, column=3)
clear_output_file.grid(row=1, column=4)

lbl_output_config.grid(row=2, column=1)
disp_output_config.grid(row=2, column=2)
choose_output_config.grid(row=2, column=3)
clear_output_config.grid(row=2, column=4)

grp_heading.grid(row=1, column=1)

lbl_heading_font.grid(row=1, column=1)
disp_heading_font.grid(row=1, column=2)
choose_heading_font.grid(row=1, column=3)
clear_heading_font.grid(row=1, column=4)

lbl_heading_font_size.grid(row=2, column=1)
disp_heading_font_size.grid(row=2, column=2)

lbl_heading_text.grid(row=3, column=1)
disp_heading_text.grid(row=3, column=2)
lbl_subheading_text.grid(row=4, column=1)
disp_subheading_text.grid(row=4, column=2)

grp_text.grid(row=2, column=1)

lbl_font.grid(row=1, column=1)
disp_font.grid(row=1, column=2)
choose_font.grid(row=1, column=3)
clear_font.grid(row=1, column=4)

lbl_font_size.grid(row=2, column=1)
disp_font_size.grid(row=2, column=2)

grp_backdrop.grid(row=1, column=1)
lbl_backdrop.grid(row=1, column=1)
disp_backdrop.grid(row=1, column=2)
choose_backdrop.grid(row=1, column=3)
clear_backdrop.grid(row=1, column=4)

grp_logo.grid(row=2, column=1)
lbl_logo.grid(row=1, column=1)
disp_logo.grid(row=1, column=2)
choose_logo.grid(row=1, column=3)
clear_logo.grid(row=1, column=4)

lbl_logo_width.grid(row=2, column=1)
disp_logo_width.grid(row=2, column=2)

lbl_logo_height.grid(row=3, column=1)
disp_logo_height.grid(row=3, column=2)

grp_series_logo.grid(row=3, column=1)
lbl_series_logo.grid(row=1, column=1)
disp_series_logo.grid(row=1, column=2)
choose_series_logo.grid(row=1, column=3)
clear_series_logo.grid(row=1, column=4)

disp_show_champion.grid(row=1, column=1)
disp_points_structure.grid(row=2, column=1)

ui_root.mainloop()
